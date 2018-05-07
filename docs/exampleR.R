# Microgrid R Example
#
# R Script
# 
# Loads data from dummy microgrid node A from MySQL and graphs it
# 
# Depends on "RMySQL"
#
# Install by running install.packages("RMySQL")
#
# See exampleRplot.pdf

# Include the Database Package
library(DBI)

# Connect to the MySQL Database
# This line may need to be modified based on SQL server setup
# Additional connectors exist and can be used for other databases
con <- dbConnect(RMySQL::MySQL(), dbname = "microgrid", username="microgrid", password="microgrid", host="localhost")

# List the tables available in the database we connected to
tables <- dbListTables(con)
print("Tables:")
print(tables)

# From the floating point data table, read entries from dummy microgrid node a
res <- dbGetQuery(con, "SELECT * FROM FloatMicrogridDatum WHERE _id=\"microgrid-node-a-g-3\"")

# Print a summary of the data we just received
print(summary(res))

# Launch a plot of value vs. time
plot(res$value)

# Disconnect from the database
dbDisconnect(con)	
