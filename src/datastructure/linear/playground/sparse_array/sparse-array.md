# 稀疏数组  
    当一个大的数组含有许多空元素或者相同元素时，存在存储空间的浪费。

使用稀疏数组可以解决空白或者大量重复元素的空间浪费问题。
如下所所示的数组： 

[0, 0, 0, 0, 0, 0]  
[0, 8, 3, 0, 0, 0]  
[0, 0, 5, 0, 0, 0]  
[0, 1, 0, 0, 0, 0]  
[0, 0, 0, 0, 9, 0]  

稀疏数组通过记录数组的维度，不同元素的个数
将不同元素的位置索引记录在一个小规模的数组中
，从而实现规模压缩。下面是上面数组的稀疏数组。  
[5, 6, 4]  
[1, 1, 8]  
[1, 2, 3]  
[2, 2, 5]  
[3, 1, 1]  
[4, 4, 9]  
数组的第一行存储的是维度和不同元素个数
其余各行分别存储了元素的索引和值。


