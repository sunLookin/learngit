# -*- coding: utf-8 -*-
"""
Spyder Editor

This is a temporary script file.
"""
import sys
class Main:
    
    def isInternalfour(self):
        a = []
        strTranslationInt = input("输入值:")
        #这个for是用来判断输入是否正确,如果大于4,则强行退出程序
        for str in strTranslationInt:
            if(int(str,10) > 4):
                print("输入错误")
                sys.exit(0)
        #这个for是用来将输入的字符串转换成相邻的数字并保存在列表中,使用字符串的切片
        for i in range(len(strTranslationInt)):
           a.append(strTranslationInt[i:i+2])
        #删除最后一个元素,因为最后一个元素是输入的最后一个值,不符合要求
        del a[len(a) - 1]
        return a
    
    def obtainResult(self):
        repeatlist = Main.isInternalfour(self)
        print("得到彼此相邻数的集合>>")
        print(repeatlist)
        #将列表转换成集合,去除重复值,已进行下面的个数统计
        unrepeatlist = set(repeatlist)
        for i in unrepeatlist:
            count = 0
            for j in repeatlist:
                if(i==j):
                    count = count + 1
            print('数值{0}的个数为{1}'.format(i,count))

#下面的代码为自动执行该程序的代码
def main():
    obj = Main()
    obj.obtainResult()
if __name__ == '__main__':
    main()