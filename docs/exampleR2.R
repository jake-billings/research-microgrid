# exampleR2.R
# 
# R script
# 
# performs a sinusoidal regression on a the example csv export of dummy sinusoidal data
# 
# depends on exampleDataDump.csv having sinusoidal data

# Load the data from the CSV file
data = read.csv("exampleDataDump.csv", head=FALSE)

# Normalize the timestamp by subtracting the first timestamp from all timestamps
# After this operation, all timestamps are in milliseconds from the first measurement
firstTimestamp = data[1, 1]
print (firstTimestamp)
data[, 1] = data[, 1] - firstTimestamp

# Print a summary of the data
print(summary(data))

# Print the head of the data
print(head(data))

# ==Build a Sinusoidal Model==
# Break the columns into two sets purely for convenience
x = data[,1]
y = data[,2]

# Assume points are evenly spaced
# Use the first two points to determine the time between samples
millisecondsPerSample = x[2]-x[1]

#Use Fast Fourer Transform to find the loudest frequency in Hz
freqHz <- which.max(abs(fft(y))) / (1000/millisecondsPerSample) / length(y)

# Calculates the number of milliseconds in one period at the loudest frequency
millisecondsPerPeriod = 1/freqHz * 1000

# Use an inverse sine model to determine the offset/phase of the wave function
#  from the first value
offset <- asin(data[1,2]/max(data[2]))*millisecondsPerPeriod/2/pi

# Generate a set of predections to plot against the real data
predictionY <- max(y) * sin(1/millisecondsPerPeriod * 2 * pi * (x + offset))

# Plot the real data as a scatter plot
plot(x, y)
# Add the model's prediction as a solid blue line
lines(x, predictionY, col="blue")