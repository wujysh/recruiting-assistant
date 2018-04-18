import os
import tensorflow as tf
from math import sqrt


class TCNNConfig(object):
    """CNN配置参数"""

    embedding_dim = 64
    seq_length = 600
    num_classes = 8
    num_filters = 256
    kernel_size = 5000
    vocab_size = 5000
    fc_layers = [1024, 1024]

    hidden_dim = 128

    dropout_keep_prob = 0.5
    learning_rate = 1e-3

    batch_size = 64
    num_epochs = 20

    print_per_batch = 100
    save_per_batch = 10


class TextCNN(object):
    """文本分类，CNN模型"""

    def __init__(self, config):
        self.config = config

        self.input_x = tf.placeholder(tf.int32, [None, self.config.seq_length], name='input_x')
        self.input_y = tf.placeholder(tf.float32, [None, self.config.num_classes], name='input_y')
        self.keep_prob = tf.placeholder(tf.float32, name='keep_prob')

        self.cnn()

    def cnn(self):
        os.environ["CUDA_VISIBLE_DEVICES"] = "0"
        with tf.device('/gpu:0'):
            self.W = tf.Variable(tf.random_uniform([self.config.vocab_size, self.config.embedding_dim], -1.0, 1.0),
                                 name='W')
            self.embedding_chars = tf.nn.embedding_lookup(self.W, self.input_x)
            self.embedding_chars_expanded = tf.expand_dims(self.embedding_chars, -1)

        with tf.name_scope('cnn'):
            # filter_sizes = [[7, 3], [7, 3], [3, None], [3, None], [3, None], [3, 3]]
            filter_sizes = [[3], [4], [5]]

            for i, filter_size in enumerate(filter_sizes):
                with tf.name_scope('conv-maxpool-%s' % str(i + 1)):
                    filter_width = self.embedding_chars_expanded.get_shape()[2].value
                    filter_shape = [filter_size[0], filter_width, 1, self.config.num_filters]

                    stdv = 1 / sqrt(filter_size[0] * self.config.num_filters)
                    W = tf.Variable(tf.random_uniform(filter_shape, minval=-stdv, maxval=stdv), name='W')
                    # W = tf.Variable(tf.truncated_normal(filter_shape, stddev=0.1/0.05), name='W')
                    b = tf.Variable(tf.random_uniform(shape=[self.config.num_filters], minval=stdv, maxval=stdv),
                                    name='b')
                    # b = tf.Variable(tf.constant(0.1, shape=[filter_size[0]]), name="b")
                    # b = tf.Variable(tf.constant(0.1, shape=[self.config.num_filters]), name='b')
                    conv = tf.nn.conv2d(self.embedding_chars_expanded, W, strides=[1, 1, 1, 1], padding='VALID',
                                        name='conv')

                    h = tf.nn.bias_add(conv, b)

                    if not filter_size[-1] is None:
                        ksize_shape = [1, filter_size[-1], 1, 1]
                        h_pool = tf.nn.max_pool(h, ksize=ksize_shape, strides=ksize_shape, padding='VALID', name='pool')
                    else:
                        h_pool = h

                    self.embedding_chars_expanded = tf.transpose(h_pool, [0, 1, 3, 2], name='transpose')

            with tf.name_scope('reshape'):
                fc_dim = self.embedding_chars_expanded.get_shape()[1].value * self.embedding_chars_expanded.get_shape()[
                    2].value
                self.embedding_chars_expanded = tf.reshape(self.embedding_chars_expanded, [-1, fc_dim])

            weights = [fc_dim] + self.config.fc_layers

            for i, fc_layer in enumerate(self.config.fc_layers):
                with tf.name_scope('fc_layer-%s' % fc_layer):
                    stdv = 1 / sqrt(weights[i])
                    # W = tf.Variable(tf.random_uniform([weights[i], fc_layer], minval=-stdv, maxval=stdv),
                    #                 dtype='float32', name='W')
                    # b = tf.Variable(tf.random_uniform(shape=[fc_layer], minval=-stdv, maxval=stdv), dtype='float32',
                    #                 name='b')
                    # 不同的初始化方式
                    W = tf.Variable(tf.truncated_normal([weights[i], fc_layer], stddev=0.05), name="W")
                    b = tf.Variable(tf.constant(0.1, shape=[fc_layer]), name="b")
                    self.embedding_chars_expanded = tf.nn.relu(tf.matmul(self.embedding_chars_expanded, W) + b)

                    with tf.name_scope('drop_out'):
                        self.embedding_chars_expanded = tf.nn.dropout(self.embedding_chars_expanded, self.keep_prob)

            with tf.name_scope('score'):
                stdv = 1 / sqrt(weights[-1])
                W = tf.Variable(
                    tf.random_uniform([self.config.fc_layers[-1], self.config.num_classes], minval=-stdv, maxval=stdv),
                    dtype='float32', name='W')
                b = tf.Variable(tf.random_uniform(shape=[self.config.num_classes], minval=-stdv, maxval=stdv), name='b')
                self.logits = tf.nn.xw_plus_b(self.embedding_chars_expanded, W, b, name="scores")
                self.y_pred_cls = tf.argmax(self.logits, 1, name="output")

                self.output_y = self.y_pred_cls

            with tf.name_scope('optimize'):
                losses = tf.nn.softmax_cross_entropy_with_logits(logits=self.logits, labels=self.input_y)
                l2_reg_lambda = 0.0
                l2_loss = tf.constant(0.0)
                self.loss = tf.reduce_mean(losses) + l2_reg_lambda * l2_loss
                self.optim = tf.train.AdamOptimizer(learning_rate=self.config.learning_rate).minimize(self.loss)

                # Accuracy
            with tf.name_scope("accuracy"):
                correct_predictions = tf.equal(self.y_pred_cls, tf.argmax(self.input_y, 1))
                self.acc = tf.reduce_mean(tf.cast(correct_predictions, tf.float32), name="accuracy")
