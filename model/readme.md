## 环境要求
	python2.7
	numpy
	jieba
	sklearn

## 运行方法
	命令行输入>>python model.py question
	输出格式：
		1.code:200
		label:福利
		（代表输出正确）
		2.code:400（代表该问题无匹配标签，该问题无法回答）
		3.code:401
（代表该问题问法较奇怪，请换一种常见表述）

## 示例
	1.python model.py 百度的福利待遇好吗
		code:200
		label:福利

	2.python model.py 哈哈哈
		code:400

	3.python model.py 百度有多少人
		code:401
