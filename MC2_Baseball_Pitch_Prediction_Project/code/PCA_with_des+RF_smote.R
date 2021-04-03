#Libraries required. install.packages("caret") to install any package
library(caret)
library(e1071)
library(rpart)
library(randomForest)
library(ggplot2)
library(rattle)
library(rpart.plot)
library(RColorBrewer)
library(ROSE)
library(DMwR)
library(ROCR)
library(UBL)
library(party)
library(factoextra)
library(ggfortify)
library(stringr)


#Reading the dataset

data <- read.csv(file.choose(),header = T,sep=",")

#retrieving the onlu 16 important features from data

data <- data[,c(20,29:33,38:46,58,59)]

#Checking the structure and summary of data
str(data)
summary(data)

#Changing these columns to numeric
data$Pitch.Break..V...in. <- as.numeric(data$Pitch.Break..V...in.)
data$Pitch.Break.Ind..V...in. <- as.numeric(data$Pitch.Break.Ind..V...in.)
data$Pitch.Break..H...in. <- as.numeric(data$Pitch.Break..H...in.)

#Removing the rows where pitch type is empty. 
#converting the fourseam fastball pitch type to just fastball type.
#Removing rows with cutter type.

data <- data[!(as.character(data$Pitch.Type) == ""),]
data$Pitch.Type <- factor(data$Pitch.Type)
levels(data$Pitch.Type)

data$Pitch.Type <- str_remove(data$Pitch.Type,"Four Seam ")
data$Pitch.Type <- factor(data$Pitch.Type)
levels(data$Pitch.Type)


data <- data[!(data$Pitch.Type == "Cutter"),]
data$Pitch.Type <- factor(data$Pitch.Type)
levels(data$Pitch.Type)


#Checking no of NA's present in the dataset in whole dataset
sum(is.na(data))

#Removing the NA's

for (i in 2:ncol(data)) {
  data[,i] <- abs(data[,i])
  data[is.na(data[,i]),i] <- mean(abs(data[!is.na(data[,i]),i]))
  
}

#Rechecking the NA's
sum(is.na(data))


#Diving the data into train and test


set.seed(194)
ran <- sample(1:nrow(data), 0.75 * nrow(data)) 


##extract training set
data_train <- data[ran,-1] 

##extract testing set
data_test <- data[-ran,-1] 

##extract 3th column of train dataset because it will be used as 'cl' argument in knn function.
data_train_category <- data[ran,1]

##extract 3th column if test dataset to measure the accuracy
data_test_category <- data[-ran,1]


#Running the PCA in training set

data_train_pca <- prcomp(data_train, scale. = T)
names(data_train_pca)

#Mean & SD of variables
data_train_pca$center
data_train_pca$scale

#The rotation measure provides the principal component loading. Each column of rotation matrix contains the principal component loading vector.

data_train_pca$rotation


# the matrix x has the principal component score vectors in a 6192 × 16 dimension.

dim(data_train_pca$x)

#Variance of differenr principal components
fviz_eig(data_train_pca,addlabels=TRUE)

#Lets plot the principal components


autoplot(data_train_pca, data = data.frame(data_train,Pitch_Type = data_train_category), colour = 'Pitch_Type',
         loadings = TRUE, loadings.colour = 'blue',
         loadings.label = TRUE, loadings.label.size = 3)

#We aim to find the components which explain the maximum variance. 
#This is because, we want to retain as much information as possible using these components. 
#So, higher is the explained variance, higher will be the information contained in those components.

#compute standard deviation of each principal component
std_dev <- data_train_pca$sdev

#compute variance
 pr_var <- std_dev^2

#check variance of first 10 components
pr_var[1:10]


#To compute the proportion of variance explained by each component, we simply divide the variance by sum of total variance. 
   #proportion of variance explained
prop_varex <- pr_var/sum(pr_var)
prop_varex[1:10]


#the first component explains 22% variance


#cumulative scree plot
plot(cumsum(prop_varex), xlab = "Principal Component",
       ylab = "Cumulative Proportion of Variance Explained",
       type = "b")

#As we can see that the 90% of variance is expained by the first ten components



#training the model

-------------------------
#1. Decision tree  

set.seed(194)  

#add a training set with principal components

train_data <- data.frame( data_train_pca$x,Pitch_Type = data_train_category)


#we are interested in first 10 PCAs
train_data <- train_data[,c(1:10,17)]
str(train_data)

#run a decision tree

Dtree <- ctree(Pitch_Type ~ PC1+PC2+PC3+PC4+PC5+PC6+PC7+PC8+PC9+PC10,
               data = train_data,controls = ctree_control(mincriterion = 0.99,minsplit = 1000,maxsurrogate = 3))
Dtree
plot(Dtree)
#Another decision tree using different function
rpart_model <- rpart(Pitch_Type ~ PC1+PC2+PC3+PC4+PC5+PC6+PC7+PC8+PC9+PC10,
                      data = train_data)
 #rpart_model
#rpart.plot(rpart_model,extra = 106)
#transform test into PCA
 test_data <- predict(data_train_pca, newdata = data_test)
 test_data <- as.data.frame(test_data)

#select the first 10 components
 test_data <- test_data[,1:10]

#make prediction on test data
rpart_prediction <- predict(rpart_model, test_data,type="class")
rpart_prediction


tab <- table(rpart_prediction,data_test_category)
tab
accuracy <- sum(diag(tab))/sum(tab)
accuracy

#2. Random Forest

set.seed(194)
RF_Model <- randomForest(Pitch_Type ~ PC1+PC2+PC3+PC4+PC5+PC6+PC7+PC8+PC9+PC10,
                         data = train_data, ntree = 100 , importance = TRUE)
RF_Pred <- predict(RF_Model, test_data, type = "class")

# confusion matrix


confusionMatrix(table(RF_Pred, data_test_category))

#variable importance
varImpPlot(RF_Model)

#Partial dependent plot

partialPlot(RF_Model,train_data,PC1,"Fastball")


#Tune RF_Model

tuneRF(train_data[,1:10],train_data[,11],stepFactor = 1,plot = T,ntreeTry = 100,trace = T,improve = 0.05)

#We didn't see much changes in mtry and ntree. it's taken as 3 so no parameter change can increase
# accuracy of random forest




#4.Naive Bayes ---------------------

library(e1071)
set.seed(194)

Naive_Model <- naiveBayes(Pitch_Type ~ .,data = train_data)

#Make predictions in test data

Naive_Pred <- predict(Naive_Model,test_data,type = "class")

#Performance Matrics

confusionMatrix(Naive_Pred,data_test_category)





#-----------------------------------------------

#-------------------------------------------------------------

# Using SMOTE to correct the data imbalancement and then trying with all four models again

#Checking the proportion of Different pitch type

table(data$Pitch.Type)
100*prop.table(table(data$Pitch.Type))

#converting whole data under PCA
data_pca <- prcomp(data[2:17], scale. = T)
data_pca <- data.frame( data_pca$x,Pitch_Type = data$Pitch.Type)

#Using SmoteClassif

data_pca_smote <- SmoteClassif(Pitch_Type ~ .,data_pca,C.perc = "balance",k=5)
table(data_pca_smote$Pitch_Type)
100*prop.table(table(data_pca_smote$Pitch_Type))

Ind_smote <- createDataPartition(y = data_pca_smote$Pitch_Type,p=0.75,list =F)
train_pca_smote <- data_pca_smote[Ind_smote,]
test_pca_smote <- data_pca_smote[-Ind_smote,]

table(test_pca_smote$Pitch_Type)

#we are interested in first 10 PCAs
train_data <- train_pca_smote[,c(1:10,17)]
str(train_data)

#1. Decision Tree

set.seed(194)

DT_Model <- ctree(Pitch_Type ~ PC1+PC2+PC3+PC4+PC5+PC6+PC7+PC8+PC9+PC10,
                  data = train_data,controls = ctree_control(mincriterion = 0.99,minsplit = 50))

DT_Model

Dtree <- rpart(Pitch_Type ~ PC1+PC2+PC3+PC4+PC5+PC6+PC7+PC8+PC9+PC10,
               data = train_data)

Dtree

#select the first 10 components
test_data <- test_pca_smote[,1:10]

#make prediction on test data
rpart_prediction <- predict(DT_Model, test_data)
rpart_prediction

#Confusion Matrix And Accuracy
tab <- table(rpart_prediction,test_pca_smote[,17])
tab
accuracy <- sum(diag(tab))/sum(tab)
accuracy
confusionMatrix(rpart_prediction,test_pca_smote[,17])

# 2.random forest


set.seed(194)
RF_Model <- randomForest(Pitch_Type ~ PC1+PC2+PC3+PC4+PC5+PC6+PC7+PC8+PC9+PC10,
                         data = train_data, ntree = 100, importance = TRUE)
RF_Pred <- predict(RF_Model, test_data, type = "class")

# confusion matrix
confusionMatrix(table(RF_Pred, test_pca_smote[,17]))
# variable importance
varImp(RF_Model)


#3. Cross validation (Not included in the report)


# 10-fold cross validation ---------------------------

#About GBM
#For a gradient boosting machine (GBM) model, there are three main tuning parameters:

#1.number of iterations, i.e. trees, (called n.trees in the gbm function)
#2. complexity of the tree, called interaction.depth
#3. learning rate: how quickly the algorithm adapts, called shrinkage
#4. the minimum number of training set samples in a node to commence splitting (n.minobsinnode)
#This model takes half an hour to run

#Ablout k-fold cross validation
#Randomly split your entire dataset into k"folds"
#For each k-fold in your dataset, build your model on k - 1 folds of the dataset. 
#Then, test the model to check the effectiveness for kth fold
#Record the error you see on each of the predictions
#Repeat this until each of the k-folds has served as the test set
#The average of your k recorded errors is called the cross-validation error and will serve as your performance metric for the model

set.seed(194)
t_control <-  trainControl(method = "repeatedcv", number = 10,repeats = 10, savePredictions = TRUE)
Tree_CV <- train(Pitch_Type ~PC1+PC2+PC3+PC4+PC5+PC6+PC7+PC8+PC9+PC10, data = train_data, trControl = t_control,method = "gbm",verbose = F)
Tree_CV
#Prediction
Tree_CV_Pred <- predict(Tree_CV,test_data)

#Confusion matrices
confusionMatrix(Tree_CV_Pred,test_pca_smote[,17])

#ROC curve, f-measure and accuracy
accuracy.meas(Tree_CV_Pred,test_Bal$Class)
roc.curve(Tree_CV_Pred,data_test_category,plotit = T)


#NAive Bayes -----------------------

library(e1071)
set.seed(194)


Naive_Model <- naiveBayes(Pitch_Type ~ .,data = train_data)

#Make predictions in test data

Naive_Pred <- predict(Naive_Model,test_data,type = "class")

#Performance Matrics

confusionMatrix(Naive_Pred,test_pca_smote[,17])



