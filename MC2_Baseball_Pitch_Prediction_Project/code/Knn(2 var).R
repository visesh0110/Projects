library(stringr)
library(class)

#Reading the dataset

data <- read.csv(file.choose(),header = T,sep = ",")
str(data)

data <- data[,c(1:14,16,20,22,29:49,54,57:70,75,76,77)]
str(data)

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

#------------------------------
 # --------------------------------------
  #------------------------------------------------
  
  # 1. two variable with horizontal and vertical movement
  
  
str(data)
data[is.na(data$pfxx..in.),c("pfxx..in.")] <- mean(abs(data[!is.na(data$pfxx..in.),c("pfxx..in.")]))
data[is.na(data$pfxz..in.),c("pfxz..in.")] <- mean(abs(data[!is.na(data$pfxz..in.),c("pfxz..in.")]))

summary(data)

#Removing the outliers from the data

data$pfxx..in. <- abs(data$pfxx..in.)
data$pfxz..in. <- abs(data$pfxz..in.)


quan_x25 <- quantile(data$pfxx..in.,0.25)
quan_x75 <- quantile(data$pfxx..in.,0.75)
quan_x_iqr <- quan_x75 - quan_x25

quan_z25 <- quantile(data$pfxz..in.,0.25)
quan_z75 <- quantile(data$pfxz..in.,0.75)
quan_z_iqr <- quan_z75 - quan_z25


r1 <- c()

for (i in 1:nrow(data)) {
  if((data$pfxx..in.[i] < (quan_x25 - 1.5 * quan_x_iqr)) | (data$pfxx..in.[i] > (quan_x75 + 1.5 * quan_x_iqr)))
  {
    r1 <- c(r1,i)
  }
  
  else if((data$pfxz..in.[i] < (quan_z25 - 1.5 * quan_z_iqr)) | (data$pfxz..in.[i] > (quan_z75 + 1.5 * quan_z_iqr)))
  {
    r1 <-  c(r1,i)
  }
}

r1
data_knn <- data[-r1,]


data_knn <- data_knn[,c("pfxx..in.","pfxz..in.","Pitch.Type")]
data_knn$Pitch.Type <- factor(data_knn$Pitch.Type)
levels(data_knn$Pitch.Type)
head(data_knn)

#Cheking the NA's value
sum(is.na(data_knn))



##the normalization function is created
nor <-function(x) { (x -min(x))/(max(x)-min(x))   }

##Run nomalization on 2 coulumns of dataset  
data_knn_norm <- as.data.frame(lapply(data_knn[,c(1,2)], nor))
data_knn_norm
summary(data_knn_norm)

##Generate a random number that is 75 of the total number of rows in dataset.

set.seed(194)
ran <- sample(1:nrow(data_knn), 0.75 * nrow(data_knn)) 

##extract training set
data_train <- data_knn_norm[ran,] 

##extract testing set
data_test <- data_knn_norm[-ran,] 

##extract 3th column of train dataset because it will be used as 'cl' argument in knn function.
data_train_category <- data_knn[ran,3]

##extract 3th column if test dataset to measure the accuracy
data_test_category <- data_knn[-ran,3]


##run knn function


knn <- knn(data_train,data_test,cl=data_train_category,k=17)

##create confusion matrix
tab <- table(knn,data_test_category)
tab


##this function divides the correct predictions by total number of predictions that tell us how accurate teh model is.

accuracy <- function(x){sum(diag(x)/(sum(rowSums(x)))) * 100}

accuracy(tab)

# knn with different k values

i=1
knn.arr=1
for(i in 1:20){
  
  knn.mod <- knn(data_train,data_test,cl=data_train_category,k=i)
  knn.arr[i] <- 100 * sum(data_test_category == knn.mod)/NROW(data_test_category)
  k=i
  cat(k,' = ',knn.arr[i],'')
  print("")
  }

#Accuracy plot
plot(knn.arr, type="b", xlab="K- Value",ylab="Accuracy level",main = "Horizontal & Vertical Movement",col = "red")


------------------------------------
  ---------------------------------------------
  ------------------------------------------------------
  
  # 2 Variable with start speed and vertical movement
  
str(data)
data[is.na(data$Pitch.Speed..mph.),c("Pitch.Speed..mph.")] <- mean(abs(data[!is.na(data$Pitch.Speed..mph.),c("Pitch.Speed..mph.")]))
  
summary(data)


data$Pitch.Speed..mph. <- abs(data$Pitch.Speed..mph.)

quan_x25 <- quantile(data$Pitch.Speed..mph.,0.25)
quan_x75 <- quantile(data$Pitch.Speed..mph.,0.75)
quan_x_iqr <- quan_x75 - quan_x25

quan_z25 <- quantile(data$pfxz..in.,0.25)
quan_z75 <- quantile(data$pfxz..in.,0.75)
quan_z_iqr <- quan_z75 - quan_z25


r1 <- c()

for (i in 1:nrow(data)) {
  if((data$Pitch.Speed..mph.[i] < (quan_x25 - 1.5 * quan_x_iqr)) | (data$Pitch.Speed..mph.[i] > (quan_x75 + 1.5 * quan_x_iqr)))
  {
    r1 <- c(r1,i)
  }
  
  else if((data$pfxz..in.[i] < (quan_z25 - 1.5 * quan_z_iqr)) | (data$pfxz..in.[i] > (quan_z75 + 1.5 * quan_z_iqr)))
  {
    r1 <-  c(r1,i)
  }
}


r1
data_knn <- data[-r1,]




data_knn <- data_knn[,c("Pitch.Speed..mph.","pfxz..in.","Pitch.Type")]
data_knn$Pitch.Type <- factor(data_knn$Pitch.Type)
levels(data_knn$Pitch.Type)
head(data_knn)

sum(is.na(data_knn))


##the normalization function is created
nor <-function(x) { (x -min(x))/(max(x)-min(x))   }

##Run nomalization on 2 coulumns of dataset  
data_knn_norm <- as.data.frame(lapply(data_knn[,c(1,2)], nor))
data_knn_norm
summary(data_knn_norm)

##Generate a random number that is 90% of the total number of rows in dataset.

set.seed(194)
ran <- sample(1:nrow(data_knn), 0.75 * nrow(data_knn)) 

##extract training set
data_train <- data_knn_norm[ran,] 

##extract testing set
data_test <- data_knn_norm[-ran,] 

##extract 3th column of train dataset because it will be used as 'cl' argument in knn function.
data_train_category <- data_knn[ran,3]

##extract 3th column if test dataset to measure the accuracy
data_test_category <- data_knn[-ran,3]

##load the package class
library(class)
##run knn function


knn <- knn(data_train,data_test,cl=data_train_category,k=1)

##create confusion matrix
tab <- table(knn,data_test_category)

tab


##this function divides the correct predictions by total number of predictions that tell us how accurate teh model is.

accuracy <- function(x){sum(diag(x)/(sum(rowSums(x)))) * 100}

accuracy(tab)

# knn with different k values

i=1
knn.arr=1
for(i in 1:20){
  
  knn.mod <- knn(data_train,data_test,cl=data_train_category,k=i)
  knn.arr[i] <- 100 * sum(data_test_category == knn.mod)/NROW(data_test_category)
  k=i
  cat(k,' = ',knn.arr[i],'')
  print("")
}

#Accuracy plot
plot(knn.arr, type="b", xlab="K- Value",ylab="Accuracy level",main = "Start Speed & Horizontal Movement",col="blue")




-------------------------
  -----------------------------------
  -----------------------------------------------
  
 #3.  two variable start speed and horizontal movement


str(data)
quan_x25 <- quantile(data$Pitch.Speed..mph.,0.25)
quan_x75 <- quantile(data$Pitch.Speed..mph.,0.75)
quan_x_iqr <- quan_x75 - quan_x25

quan_z25 <- quantile(data$pfxx..in.,0.25)
quan_z75 <- quantile(data$pfxx..in.,0.75)
quan_z_iqr <- quan_z75 - quan_z25


r1 <- c()

for (i in 1:nrow(data)) {
  if((data$Pitch.Speed..mph.[i] < (quan_x25 - 1.5 * quan_x_iqr)) | (data$Pitch.Speed..mph.[i] > (quan_x75 + 1.5 * quan_x_iqr)))
  {
    r1 <- c(r1,i)
  }
  
  else if((data$pfxx..in.[i] < (quan_z25 - 1.5 * quan_z_iqr)) | (data$pfxx..in.[i] > (quan_z75 + 1.5 * quan_z_iqr)))
  {
    r1 <-  c(r1,i)
  }
}


r1
data_knn <- data[-r1,]




data_knn <- data_knn[,c("Pitch.Speed..mph.","pfxx..in.","Pitch.Type")]
data_knn$Pitch.Type <- factor(data_knn$Pitch.Type)
levels(data_knn$Pitch.Type)
head(data_knn)

sum(is.na(data_knn))


##the normalization function is created
nor <-function(x) { (x -min(x))/(max(x)-min(x))   }

##Run nomalization on 2 coulumns of dataset  
data_knn_norm <- as.data.frame(lapply(data_knn[,c(1,2)], nor))
data_knn_norm
summary(data_knn_norm)

##Generate a random number that is 90% of the total number of rows in dataset.

set.seed(194)
ran <- sample(1:nrow(data_knn), 0.75 * nrow(data_knn)) 

##extract training set
data_train <- data_knn_norm[ran,] 

##extract testing set
data_test <- data_knn_norm[-ran,] 

##extract 3th column of train dataset because it will be used as 'cl' argument in knn function.
data_train_category <- data_knn[ran,3]

##extract 3th column if test dataset to measure the accuracy
data_test_category <- data_knn[-ran,3]

##load the package class
library(class)
##run knn function


knn <- knn(data_train,data_test,cl=data_train_category,k=1)

##create confusion matrix
tab <- table(knn,data_test_category)

tab


##this function divides the correct predictions by total number of predictions that tell us how accurate teh model is.

accuracy <- function(x){sum(diag(x)/(sum(rowSums(x)))) * 100}

accuracy(tab)

# knn with different k values

i=1
knn.arr=1
for(i in 1:20){
  
  knn.mod <- knn(data_train,data_test,cl=data_train_category,k=i)
  knn.arr[i] <- 100 * sum(data_test_category == knn.mod)/NROW(data_test_category)
  k=i
  cat(k,' = ',knn.arr[i],'')
  print("")
}

#Accuracy plot
plot(knn.arr, type="b", xlab="K- Value",ylab="Accuracy level",main = "Start Speed & Vertical Movement",col = "darkgreen")





  