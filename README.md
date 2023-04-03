# Zettel Service
* Build changes and a prod `.jar` for this project with `mvn package`
* Start the `app.jar` on the `ec2` using `sudo nohup java -jar app.jar 2>&1 >> proc/proc.log &`
* Use `jobs` to see that the process is still running
* `sudo kill % <JOB_NUMBER>` will kill the process and stop the program; you can
  also do `pgrep -a java` to get the PID and then `kill -9 <PID>`
* Use `tail -f proc/proc.log` to get a running log from the process; alternatively,
  check out the static logs produced by log4j
* If you stop the process, please clear the running logs with `rm proc/proc.log`

## Setup
Please make sure that the EC2 instance this is running on top of is configured
for SSL: [202301030038](../202301030038) - SSL Issues Resolved

Please make sure that the root user has the AWS credentials in `/root/.aws/credentials`

## Endpoints
* `GET /previews`
* `GET /zettels/{zettelId}`

## Technologies
* Using AWS S3, AWS DynamoDB, AWS EC2, AWS SSM
