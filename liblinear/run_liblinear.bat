cd liblinear\windows\
train.exe -s 0 ..\..\liblinear\train.txt model
predict.exe -b 1 ..\..\liblinear\test.txt model prediction.txt