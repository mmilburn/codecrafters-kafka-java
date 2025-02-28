# About the Project

This is a finished Java implementation for the
["Build Your Own Kafka" Challenge](https://codecrafters.io/challenges/kafka).
This code implements functionality for all stages (and extensions) of the
challenge as of 2025-02-13.

## What can it do?

1. Respond to an `APIVersions` request with supported API calls (`APIVersion`,
   `DescribeTopicPartitions`, `Fetch`).
2. Respond to `DescribeTopicPartitions` and `Fetch` requests with real data (
   Given a `server.properties` file it will parse the kafka metadata log file
   and topic data from `log.dirs`). Otherwise, it will happily respond with "I
   know nothing of these topics or their partitions."

## Running Locally

You will need Java 23 (or later) and maven installed to run this code. The
program can then be run with:

`./your_program.sh /optional/path/to/server.properties`

## Test Run Video

A short video of the code being run in the codecrafters test environment:

https://github.com/user-attachments/assets/a99f7c32-8c87-4918-8675-1bd2ab610d8f