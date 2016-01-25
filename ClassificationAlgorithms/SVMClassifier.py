__author__ = 'Anup'
from sklearn import cross_validation, metrics
from sklearn.svm import SVC
from sklearn.naive_bayes import MultinomialNB
from sklearn.multiclass import OneVsOneClassifier, OneVsRestClassifier
from sklearn.decomposition import PCA
import numpy as np
import csv
from sklearn import datasets
from sklearn.cluster import KMeans
from matplotlib import pyplot
from datetime import datetime
trainingPath="C:\Users\Anup\Desktop\DM\FinalTakeHome\genresTrain.csv"
testPath="C:\Users\Anup\Desktop\DM\FinalTakeHome\genresTest.csv"
writePath=open("C:\Users\Anup\Desktop\DM\FinalTakeHome\Result.txt","a")
genre={"Pop":0,"Blues":1,"Jazz":2,"Rock":3,"Classical":4,"Metal":5}
genreList=["Pop","Blues","Jazz","Rock","Classical","Metal"]
clf = OneVsRestClassifier(SVC(C=1, kernel = 'linear', gamma=1, verbose= False, probability=False))
#clf = MultinomialNB(alpha=0.1, class_prior=None, fit_prior=True)
count=0

def genTrainingAndLableData():
 with open(trainingPath, 'rb') as csvfile:
  readFile = csv.reader(csvfile, delimiter=',')
  trainData=[]
  lableData=[]
  labelGenre=[]
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
             labelGenre.append(value)
    trainData.append(tempArr)
  csvfile.close()
  return labelGenre



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

def runPCA(dataToReduce):
    pca = PCA(n_components=10)
    reducedData=pca.fit_transform(dataToReduce,191)
    print len(reducedData)
    return reducedData

#testData=readTestFile()
def trainAndPredict():
   training,lable= genTrainingAndLableData()
   testData=readTestFile()
   reducedTrainData=runPCA(training)
   reducedTestData=runPCA(testData)
   clf.fit(reducedTrainData[:3000], lable[:3000])

   count=0
   for data in reducedTestData:
      count+=1
      print count
      outList = clf.predict(data)

      predictedValue=[]
      for val in outList:
         predictedValue.append(genreList[val])

      writePath.write(str(predictedValue))
      writePath.write("\n")

   #print predictedValue

start= datetime.now()
trainAndPredict()
end= datetime.now()
print "Time Taken "+str(end-start)

"""
iris = datasets.load_iris()
iris.data
iris.target
print "Hi"
"""


def runKmens(K):
    training,lable= genTrainingAndLableData()
    data=np.array(training)
    testData=readTestFile()
    test=np.array(testData)
    #y_pred = KMeans(n_clusters=K).fit_predict(data)
    y_pred = KMeans(n_clusters=K).fit(data)
    print y_pred.predict(data)
    return y_pred.cluster_centers_


#len(runKmens(8))

def runKmenasvisual(K):
    training,lable= genTrainingAndLableData()
    data=np.array(training)
    y_pred = KMeans(n_clusters=K).fit(data)
    labels = y_pred.labels_
    centroids = y_pred.cluster_centers_
    for i in range(K):
        #select only data observations with cluster label == i
        ds = data[np.where(labels==i)]
        # plot the data observations
        pyplot.plot(ds[:,0],ds[:,1],'o')
        # plot the centroids
        lines = pyplot.plot(centroids[i,0],centroids[i,1],'kx')
        # make the centroid x's bigger
        pyplot.setp(lines,ms=15.0)
        pyplot.setp(lines,mew=2.0)
    pyplot.show()
"""
def runPCA():
    training,lable= genTrainingAndLableData()
    pca = PCA(n_components=100)
    reducedData=pca.fit_transform(training,191)
    print len(reducedData[0])
    print len(reducedData)

runPCA()
"""

listOfLables=genTrainingAndLableData()
dictMy={}
for i in listOfLables:
    if dictMy.has_key(i):
        count=dictMy[i]
        count+=1
        dictMy[i]=count
    else:
        dictMy[i]=1

print dictMy
#runKmenasvisual(23)
