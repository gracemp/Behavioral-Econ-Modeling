require(dplyr)
require(reshape2)
require(tidyr)
 numConditions <- 6
 numSub <- 91
 find.avg.z <- function(a,b,c,d) {
  sum(a,b,c,d)/4
 }
 find.dif <- function(a,b){
   b-a
 }
 
 
#data manipulation
rawData <- read.csv("raw_data.csv", header = TRUE, stringsAsFactors = FALSE)
#colnames(rawData) <- rawData[1,]
subData <- slice(rawData,  31:66)
subData <- rbind(rawData[1], subData)
dat <- data.frame(do.call(rbind, strsplit(as.vector(subData$valid.participants), split = "% ")))
names(dat) <- c("percent_chance", "delay")
subData <- cbind(subData, dat[1])
subData <- cbind(subData, dat[2])
subData$percent_chance <- ifelse(subData$percent_chance %in% "a 100", "0",
                        ifelse(subData$percent_chance %in% "a 98", "0.0204",
                               ifelse(subData$percent_chance %in% "an 85", "0.176",
                                      ifelse(subData$percent_chance %in% "a 70", "0.4286",
                                             ifelse(subData$percent_chance %in% "a 50", "1",
                                                    ifelse(subData$percent_chance %in% "a 35", "1.857",
                                               ""))))))
subData$delay <- ifelse(subData$delay == "chance now", "0",
                              ifelse(subData$delay == "chance in 1 month", "1",
                                     ifelse(subData$delay == "chance in 6 months", "6",
                                            ifelse(subData$delay == "chance in 1 year", "12",
                                                   ifelse(subData$delay == "chance in 3 years", "36",
                                                          ifelse(subData$delay == "chance in 5 years", "60",
                                                                 ""))))))
outputVolumes <- matrix(ncol=2, nrow = numSub)
subData <- subData[,-1]
subData <- subData[!sapply(subData, function(x) all(is.na(x)))]
subData <- subData[!sapply(subData, function(x) all(x == ""))]
count = 0
for(k in (1:numSub)){
count <- count +1 
testData <- subData %>% select(percent_chance, delay)
testData <- cbind(testData, subData[[k]])
names(testData)[3] <- "value"
testData$delay <- as.numeric(as.character(testData$delay))
testData$percent_chance <- as.numeric(as.character(testData$percent_chance))
testData$value <- as.numeric(as.character(testData$value))
maxOdds <- max(testData$percent_chance)
maxDelay <- max(testData$delay)
#add delayed probabilistic amount
DPA <- 96

testData[[1]] <- lapply(testData[[1]], function(x) x/maxOdds)
testData[[2]] <- lapply(testData[[2]], function(x) x/maxDelay)
testData[[3]] <- lapply(testData[[3]], function(x) x/DPA)
 M <- testData %>% spread(percent_chance, value)
 M2 <- M[,-1]
 rownames(M2) <- M[,1]
 M2[] <- data.frame(sapply(M2, function(x) as.numeric(as.character(x))))
 x <- 0
 for (i in  1:(numConditions-1)){
   for( j in 1:(numConditions-1)){
     zval <- find.avg.z(M2[i,j], M2[i+1,j], M2[i,j+1], M2[i+1,j+1])
     xval <- find.dif(as.numeric(colnames(M2)[i]), as.numeric(colnames(M2)[i+1]))
     yval <- find.dif(as.numeric(colnames(M2)[j]), as.numeric(colnames(M2)[j+1]))
     vol <- zval*xval*yval
     x <- x + vol
   }
 }
 outputVolumes[count, 1] <- x
 outputVolumes[count, 2] <- paste0("Subject " , count)
}
outputVolumes <- as.data.frame(outputVolumes)
