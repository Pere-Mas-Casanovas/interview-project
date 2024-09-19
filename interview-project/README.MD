# Notes

The assignment implements the requirements in two flavours: mocked, using the provided input in CSV format; and with Spring Data Cassandra.

To run one or the other it is only required to set the corresponding profile as active: ` local-dev`, for mocked flavour, `local-cassandra`, for Cassandra mode

> *Unfortunately, I could not test the local-cassandra flavour as expected since I am still waiting for the approval of my request to install Docker Desktop 4 in my Mac.*
> 
> *Apologies for the inconveniences*

The endpoint exposed to produce the daily summary report can be accessed in the following URL:

``http://localhost:3026/req10079266/incidents/daily-summary/{target-day}``

Running with the `local-dev` profile, there is only data for the dates provided in the sample input file: **2019-01-04**, **2019-02-04**, **2019-03-04**, **2019-04-04** and **2019-05-04** 