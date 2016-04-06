# Learning Document Embedding by Predicting N-grams for Sentiment Classification of Long Movie Reviews


This code implements the DV-ngram model proposed in iclr 2016 workshop paper:

[Bofang Li, Tao Liu, Xiaoyong Du, Deyuan Zhang and Zhe Zhao - **Learning Document Embedding by Predicting N-grams for Sentiment Classification of Long Movie Reviews**] (http://arxiv.org/abs/1512.08183) 




##Code

The original version of this code was written in C, built upon the [code](https://github.com/mesnilgr/iclr15) written by Grégoire Mesnil et al. Due to the strong interest in this work, we have rewritten the entire algorithm in **Java** for easier and more scalable use.

This code has been tested on Windows, but it should work on Linux, OSX or any other operation system with only a little changes about calling LibLinear.

Preprocessed data for IMDB dataset can be downloaded here: [unigram](http://202.112.113.8/d/DV-ngram/alldata-id_p1gram.zip), [bigram](http://202.112.113.8/d/DV-ngram/alldata-id_p2gram.zip), [trigram](http://202.112.113.8/d/DV-ngram/alldata-id_p3gram.zip)

All parameters your may need to change are in src/NN/DV.java. Your can just run it with default setting to get around 92.14% accuracy on IMDB dataset.

Note that for DV-trigram, dictionary size is large, you may need around 20G RAM to run our code in this setting.


##Acknowledgements

We want to thank Grégoire Mesnil et al. for their implementation of Paragraph Vector. Both their code and [iclr 2015 workshop paper](http://arxiv.org/abs/1412.5335) have influenced us a lot.




