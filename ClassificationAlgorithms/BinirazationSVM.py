__author__ = 'Anup'
from sklearn import preprocessing
import numpy as np
import csv
from sklearn.svm import SVC
from sklearn.multiclass import OneVsOneClassifier, OneVsRestClassifier
from datetime import datetime
import warnings
warnings.filterwarnings("ignore", category=DeprecationWarning)
trainingPath="C:\Users\Anup\Desktop\DM\FinalTakeHome\genresTrain.csv"
testPath="C:\Users\Anup\Desktop\DM\FinalTakeHome\genresTest.csv"
genre={"Pop":0,"Blues":1,"Jazz":2,"Rock":3,"Classical":4,"Metal":5}
genreList=["Pop","Blues","Jazz","Rock","Classical","Metal"]
writePath=open("C:\Users\Anup\Desktop\DM\FinalTakeHome\Result.txt","a")
clf = OneVsRestClassifier(SVC(C=1, kernel = 'linear', gamma=1, verbose= False, probability=False))
def getBinaryValues(data):

    binarizer = preprocessing.Binarizer().fit(data)
    return binarizer.transform(data)



def genTrainingAndLableData():
 with open(trainingPath, 'rb') as csvfile:
  readFile = csv.reader(csvfile, delimiter=',')
  trainData=[]
  lableData=[]
  count=0
  for row in readFile:
    if count==0:
      count+=1
      continue
    tempArr=[]
    for value in row:
         try :
          tempArr.append(float(value))
         except ValueError:
             lableData.append(genre[value])
    trainData.append(tempArr)
  csvfile.close()
  return trainData,lableData



def readTestFile():
    with open(testPath, 'rb') as csvfile:
      readFile = csv.reader(csvfile, delimiter=',')
      count=0
      testData=[]
      for row in readFile:
        if count==0:
          count+=1
          continue
        tempArr=[]
        for value in row:
             try :
               tempArr.append(float(value))
             except ValueError:
               print value
        testData.append(tempArr)
      csvfile.close()
      return testData

def trainAndPredict():
   training,lable= genTrainingAndLableData()
   testData=readTestFile()
   binarizedTrainData=getBinaryValues(training)
   print "binarization of training data done ..."
   binarizedTestData=getBinaryValues(testData)
   print "Test data binarized.."
   clf.fit(binarizedTrainData, lable)
   print "SVM has been trained ..."
   count=0
   for data in binarizedTestData:
      count+=1
      print count
      outList = clf.predict(data)

      predictedValue=[]
      for val in outList:
         predictedValue.append(genreList[val])

      writePath.write(str(predictedValue[0]))
      writePath.write("\n")

   #print predictedValue

start= datetime.now()
trainAndPredict()
end= datetime.now()
print "Time Taken "+str(end-start)