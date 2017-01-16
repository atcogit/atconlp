# xmnlp自然语言处理包


#### author：xuming(shibing624)
#### environment：jdk 1.8


1. add the rule chinese word segmentation: 2016.06.21
    - 正向最大匹配法
    - 逆向最大匹配法
    - 双向最大匹配法
2. add jieba segmentation java 2016.07.06
	- 基于 trie 树结构实现高效词图扫描
  	- 生成所有切词可能的有向无环图 DAG
  	- 采用动态规划算法计算最佳切词组合
  	- 基于 =HMM= 模型，采用 =Viterbi= (维特比)算法实现未登录词识别
3. add xmnlp 2016.07.23
	- 基于双数组 trie 树结构实现高效词图扫描存储词典
	- 最短路径分词，最短路求解采用Viterbi算法
	- 添加人名识别功能：中文人名，日本人名，音译人名识别功能
4. alter jdk from 1.7 to 1.6 2016.07.24
	- 兼容jdk1.6版本，把所有用jdk1.7甚至1.8的写法用1.6重写了
5. add organization recognition, place recognization ,ahocorasick algoritm and tire tree 20160724 xuming
	- 机构名识别功能
	- 地名识别
	- trie词典树，基于贝尔实验室的 Aho-Corasick 白皮书完成，将来可以改为双数组trie数提高效率
6. add pinyin, keyword, stopword, POS tagging, synonym dictionary and traditionary chinese to xmnlp 2016.07.24 xuming
	- 汉字转拼音功能：结果可以显示为数字音调，符号音调，无音调，声调，声母，韵母，输入法头
	- 提取关键字
	- 添加搜索停用词
	- 词性标注
	- 同义词典
	- 简繁字体转换
7. add high speed segment, HMM segment, index segment xuming 20160730
	- 急速分词器，基于双数组前缀树词典结构，查找词典的最大匹配分词
	- 二阶隐马模型分词器
	- 索引分词器
8. add crf segment , mutithread segment, nshort segment and all test  xuming 20160730
	- 条件随机场模型分词器
	- 多线程处理，配合CRF分词效果和速度有保障
	- NShort分词器
	- 全覆盖单元测试
9. add dependency parse, include 2-gram dependency model, CRF model dependency, Max Ent model dependency and all test  xuming 20160731
	- 依存句法分析工具
	- 2-gram依存模型，根据两个词的词和词性猜测它们最可能的依存关系
	- 条件随机场（CRF）句法模型
	- 最大熵（MaxEnt）句法分析器
	- 神经网络句法模型，待完善
10. add web demo xuming 20160801
	- 几种中文分词方式演示页面：精准分词方式，NLP分词，CRF、HMM分词（需要设置jvm：-Xmx1024M）
	- 关键词提取演示
	- 拼音标注
	- 繁体句子分词
	- 繁体字转简体
	- 句法分析
	- 文本推荐演示
11. add rule segment:forwardMinSeg,reverseMinSeg,biMinSeg,biMaxMinSeg,MaxNgram. xuming 20160830
    - 正向最小分词
    - 反向最小分词
    - 双向最小分词
    - 双向最大最小分词
    - 最大Ngram分值算法分词
12. add rule ambiguity test and jieba ambiguity test. xuming 20160908
    - 规则分词（正向、反向、双向匹配算法等）歧义性测试（效果不理想）
    - 结巴分词歧义性测试（效果不理想）
13. add ansj segmentation. xuming 20160928
    - ansj中文分词器
14. add word frequency statistics. xuming 20161125
    - 分词结果词语统计
15. add organization recognize. xuming 20170110
    - 添加机构名称提取WEB页面功能
16. add person name extract in web demo. xuming 20170111
    - 添加人名提取WEB页面功能并在WEB页面注释CRF分词功能
    
    
---


**xmnlp**是由一系列模型与算法组成的Java工具包，目标是普及自然语言处理在生产环境中的应用。**xmnlp**具备功能完善、性能高效、架构清晰、语料时新、可自定义的特点。

**xmnlp**提供下列功能：

> * 中文分词.
  * 最短路分词.
  * N-最短路分词
  * CRF分词.
  * 索引分词.
  * 极速词典分词.
  * 用户自定义词典.
> * 词性标注.
> * 命名实体识别.
  * 中国人名识别.
  * 音译人名识别.
  * 日本人名识别.
  * 地名识别.
  * 实体机构名识别.
> * 关键词提取.
  * TextRank关键词提取.
> * 自动摘要.
  * TextRank自动摘要.
> * 短语提取.
  * 基于互信息和左右信息熵的短语提取.
> * 拼音转换.
  * 多音字.
  * 声母.
  * 韵母.
  * 声调.
> * 简繁转换.
  * 繁体中文分词.
  * 简繁分歧词.
> * 文本推荐.
  * 语义推荐.
  * 拼音推荐.
  * 字词推荐.
> * 依存句法分析.
  * 基于神经网络的高性能依存句法分析器.
  * MaxEnt依存句法分析.
  * CRF依存句法分析.
> * 语料库工具.
  * 分词语料预处理.
  * 词频词性词典制作.
  * BiGram统计.
  * 词共现统计.
  * CoNLL语料预处理.


在提供丰富功能的同时，**xmnlp**内部模块坚持低耦合、模型坚持惰性加载、服务坚持静态提供、词典坚持明文发布，使用非常方便，同时自带一些语料处理工具，帮助用户训练自己的语料。

------



## 如何获取
  
  - 方式一：下载源码、data

  	数据与程序分离，给予用户自定义的自由。

  	1、下载代码

  	  直接fork该项目，在git上clone到本地'https://github.com/shibing624/xmnlp.git' 或者下载源码xmnlp-master.zip

  	2、下载data

	  [data.zip](http://pan.baidu.com/s/1jIydVsq)
	  下载后解压到任意目录，接下来通过配置文件告诉xmnlp数据包的位置。

	  xmnlp中的数据分为词典和模型，其中词典是词法分析必需的，模型是句法分析必需的。


	>
	> 	  data
	> 	  │
	> 	  ├─dictionary
	> 	  └─model

	  用户可以自行增删替换，如果不需要句法分析功能的话，随时可以删除model文件夹。

	  模型跟词典没有绝对的区别，隐马模型被做成人人都可以编辑的词典形式，不代表它不是模型。
	  GitHub代码库中已经包含了data.zip中的词典，直接编译运行自动缓存即可；模型则需要额外下载。

  	3、配置文件

	  示例配置文件:`xmnlp.properties` 在src的resources中。

	  配置文件的作用是告诉xmnlp数据包的位置，只需修改第一行

	  root=usr/home/xmnlp/
	  为data的父目录即可，比如data目录是/Users/root/Documents/data，那么root=/Users/root/Documents/ 。

	  如果选用mini词典的话，则需要修改配置文件：

	  CoreDictionaryPath=data/dictionary/CoreNatureDictionary.mini.txt
	  BiGramDictionaryPath=data/dictionary/CoreNatureDictionary.ngram.mini.txt

	  最后将xmnlp.properties放入classpath即可，对于任何项目，都可以放到src或resources目录下，编译时IDE会自动将其复制到classpath中。

	  如果放置不当，xmnlp会智能提示当前环境下的合适路径，并且尝试从项目根目录读取数据集。

- 方式二：当前稳定版本（暂未使用）

    ```
  	<dependency>
            <groupId>com.xm</groupId>
            <artifactId>xmnlp</artifactId>
            <version>1.3</version>
    </dependency>
  	```

    零配置，即可使用基本功能（除CRF分词、依存句法分析外的全部功能）。如果用户有自定义的需求，可以参考方式二，使用xmnlp.properties进行配置。


## 调用方法

**xmnlp**几乎所有的功能都可以通过工具类`Xmnlp`快捷调用，当你想不起来调用方法时，只需键入`Xmnlp.`，IDE应当会给出提示，并展示**xmnlp**完善的文档。

*推荐用户始终通过工具类`Xmnlp`调用，这么做的好处是，将来**xmnlp**升级后，用户无需修改调用代码。*

所有Demo都位于[org.xm.xmnlp.demo](https://github.com/shibing624/xmnlp/tree/master/src/test/java/org/xm/xmnlp/demo)下，比文档覆盖了更多细节，强烈建议运行一遍。

#### 如何使用

  - Demo


	```
	System.out.println(Xmnlp.segment("你好，欢迎使用xmnlp自然语言处理包！"));
	```

## 支持结巴中文分词模式
   - Search模式，用于对用户查询词分词
   - Index模式，用于对索引文档分词

#### 特性
   - 支持多种分词模式
   - 全角统一转成半角
   - 用户词典功能
   - resource 目录有整理的搜狗细胞词库和一个自定义词库，可加载多个用户词库

#### 算法
  - [ ] 基于 =trie= 树结构实现高效词图扫描
  - [ ] 生成所有切词可能的有向无环图 =DAG=
  - [ ] 采用动态规划算法计算最佳切词组合
  - [ ] 基于 =HMM= 模型，采用 =Viterbi= (维特比)算法实现未登录词识别

#### 性能评估
  - 测试机配置
	```
	Processor 2 Intel(R) Pentium(R) CPU G620 @ 2.60GHz
	Memory：8GB

	分词测试时机器开了许多应用(eclipse、emacs、chrome...)，可能
	会影响到测试速度
	```

  - 测试结果(单线程，对测试文本逐行分词，并循环调用上万次)
	```
	  循环调用一万次
	  第一次测试结果：
	  time elapsed:12373, rate:2486.986533kb/s, words:917319.94/s
	  第二次测试结果：
	  time elapsed:12284, rate:2505.005241kb/s, words:923966.10/s
	  第三次测试结果：
	  time elapsed:12336, rate:2494.445880kb/s, words:920071.30/s

	  循环调用2万次
	  第一次测试结果：
	  time elapsed:22237, rate:2767.593144kb/s, words:1020821.12/s
	  第二次测试结果：
	  time elapsed:22435, rate:2743.167762kb/s, words:1011811.87/s
	  第三次测试结果：
	  time elapsed:22102, rate:2784.497726kb/s, words:1027056.34/s
	  统计结果:词典加载时间1.8s左右，分词效率每秒2Mb多，近100万词。

	  2 Processor Intel(R) Core(TM) i3-2100 CPU @ 3.10GHz
	  12G 测试效果
	  time elapsed:19597, rate:3140.428063kb/s, words:1158340.52/s
	  time elapsed:20122, rate:3058.491639kb/s, words:1128118.44/s
	```

## 鸣谢
  - HanLP 项目 
  - ansj_seg 项目 
  - jieba 项目 
  - word 项目 

## 许可证
  许可证为ApacheLicence 2.0


        Copyright (C) 2016 xm Inc

        Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

        Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.



