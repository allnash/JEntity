J* Entity
==================
Project to create N-Schemas of JSON and validate them.

##This project uses following OpenSource Projects
* Jackson JSON
* org.everit JSON Schema
* playframework

## Requirements
* Java 8.0+
* Activator lib (included)
* GIT
* Apache CouchDB (Document JSON Store, you can change code to use CouchBase or PGSQL)

	`git clone https://github.com/allnash/JEntity.git`
	
	`cd JEntity`

# Install Apache CouchDB

Create a Couch DB database
(Optionally if you already have a couch db user/password please create a new acccount
  username: admin
  password: password

using the Web Console URL:
	`http://127.0.0.1:5984/_utils/`
	
On the right hand side in the coloumn look all the way to the bottom, there are two links sign up and login, Use Sign up and create an account. Or do the below, its easier)

	export HOST="http://127.0.0.1:5984"
	
	curl -X PUT $HOST/database
	{"ok":true}

Create a new account by doing this.

	curl -X PUT $HOST/_config/admins/admin -d '"password"'

Create database db-test in CouchDB and use the username and password above. Modify conf/application.conf if you need to

# Set up project

        ./prepare_to_develop.sh

# Start activator

	./activator run

Hit the Web console

	http://localhost:9000

For the first HTTP request it will pop up a red screen and ask you to run a SQL script and migrate the database.
Don't worry, this will set up your database and seed it will all accounts, messages, notification, etc etc.   

# Add an Owner For Data and Schemas

Create a generic Owner, everthing in this world has ownership. Earth is owned by Mother Nature. And likewise
	
	curl -X POST -H "Content-Type: application/json" -d '{"owner":{"external_id":"test"}}' http://localhost:9000/owners
	
In the command below we will add a new Type `device` in our System.

	curl -X POST -H "Content-Type: application/json" -d '{"json_schema":{"title":"device","type":"object","properties":{"id":{"type":"string"},"power_on":{"type":"string"},"meter_reading":{"description":"Meter Reading for Water","type":"integer","minimum":0}},"required":["id","power_on"]}}' http://locahost:9000/

# Post Data, do Validation against Schema

In the Following Example we create a test device, notice the data type of the JSON is `device`
	
	curl -X POST -H "Content-Type: application/json" -d '{"device":{"name":"Test Device","power_on":"ON","meter_reading":10}}' http://localhost:9000/external/owners/test/devices
