I think the organization for the database is pretty much settled now, so I wanted to give a brief overview of what tables are in the database, what they hold, and what they do.

**KEY TERMS:**

**(INT)** means this column MUST hold a number, any letters will ruin your query, but because it's a number you can add or do other arithmetic to it.

**(PRIMARY KEY)** means it's the column SQL uses to identify any row on the table, that means that it can never be empty for any row, and it must be unique.

**(NOT NULL)** means it cannot be left empty when inserting new data, however, if the column has auto increment or a default value, you don't have to enter it in your INSERT query and it will fill it in on it's own.

**(AUTO INCREMENT)** means that if you don't enter in a value for this column, it will automatically choose the next highest unique number to fill it's value.

**(VARCHAR)** means that it holds a string, you can't do math to the value, but it can hold letters, numbers and symbols.

**(DATETIME)** means it has to hold a date and a time, for most of the queries that we will be using, we generally just need to enter "CURRENT_TIMESTAMP" so the database knows the exact time you sent your INSERT or UPDATE query to the database.

#PARTS#

##DESCRIPTION:##

This table lists all the parts used within the company, and all the information for that part. This is basically a reference guide for any part that was or currently is being used at the company. Parts on this list may or may not be present in the stockroom, but every part in the stockroom must be on this table.

COLUMNS:

**parts_id (INT) (PRIMARY KEY) (AUTO INCREMENT) (NOT NULL) (This column is referenced in the STOCKROOM, PRODUCT_BOM, and ORDER_ITEMS tables) :** This is the unique identifier for every part that is on this list. This is set to auto-increment every time a part is added to this list, so you don't have to enter a number for it and it will still add a unique number for it.

**part_number (INT) :** This is similar to parts_id, but this is a chosen number used within the company for the part. Two different parts with different part IDs can share the same part number as long as they share the same function. (for instance: a AA Duracel Battery and a AA Energizer Battery would have different part IDs and different vendors, but are basically the same part)

** part_description (VARCHAR) : ** Explains what the part is, it could be a name or a description.

** vendor (VARCHAR) : ** The company used to purchase this part.

** picture (VARCHAR) : ** This column will hold a link to a picture of the part.

** location (VARCHAR) : ** this will describe where it's located in the stockroom (in example: third shelf on the left)

** low_quantity (INT) : ** If this part's quantity in the STOCKROOM table is less than this number, it needs to display on the Purchasing.java page.

**STOCKROOM**

DESCRIPTION:

The stockroom explains what parts are currently in the stockroom and in what quantity.

COLUMNS:

**parts_id (INT) (PRIMARY KEY) (NOT NULL) (This column references the PARTS table) : ** The part that is in the stockroom

**quantity (INT) (NOT NULL) : ** How many of this are in the stockroom, not in the kits.

**PRODUCTS**

DESCRIPTION:

This table is used to hold the list of the different products that the company makes. When the user selects to create a new Bill of Materials for a new product, the name of that product goes here, along with the date of when that product was created. The product ID from this table is used to reference the rows of the PRODUCT_BOM table that are used for building that product.

COLUMNS:

**product_id (INT) (PRIMARY KEY) (AUTO INCREMENT) (NOT NULL) (This column is referenced in the WORKORDERS, PRODUCT_BOM, and ORDER_ITEMS tables) : ** A Unique identifier for the product in the company. Use this ID to query the PRODUCT_BOM table to make new work orders in the ORDER_ITEMS table.

**product_name (VARCHAR) (NOT NULL) : ** The name of the product that was created.

**date_created (NOT NULL) (Default Value: CURRENT_TIMESTAMP) : ** When this new product was entered into the database.

**PRODUCT_BOM**

DESCRIPTION:

BOM stands for BILL OF MATERIALS, it's the technical term for a kit. It's a list of all the parts that are needed to create a part for a particular product and the amount needed of each part to build exactly one of that product. This table will be really big, because every required part of every product will have a row here. You will have to do queries that select product ID that you want to select only those rows that you need. I originally wanted to create a separate table for each new product, but SQL has a hard time handling lots of new tables, so this is every table for every product, one after the other.

COLUMNS:

**id (INT) (PRIMARY KEY) (AUTO INCREMENT) (NOT NULL) : ** A unique identifier for each row of the table. Not important unless you need to delete one particular row of a BOM.

**product_id (INT) (NOT NULL) (This column references the PRODUCT table) : ** This column tells you the product that each particular row is meant to build.

**parts_id (INT) (NOT NULL) (This column references the PARTS table) : ** This column is used to reference the part used in the company.

**quantity (INT) : ** How many of this particular part is used to build the selected product.

**WORKORDERS**

DESCRIPTION:

This table will hold the list of all work orders that were made to the company. If the company gets an order for a particular product, it will add a new order to the WORKORDERS table, insert the product ID the order was for and how many of that product was ordered. The status will be set to "CREATED" and a CURRENT_TIMESTAMP will be sent to the date_created column, all other dates will be kept NULL until the status changes. When you add a new work order for a product, you must add all the parts from that product's PRODUCT_BOM rows to the ORDER_ITEMS table, with the PRODUCT_BOM's quantity multiplied by the WORKORDERS' quantity.

COLUMNS:

**order_id (INT) (PRIMARY KEY) (AUTO INCREMENT) (NOT NULL) (this column is referenced in the ORDER_ITEMS table) : ** A unique identifier for every order made to the company, use this ID to query the ORDER_ITEMS table when you need to kit a work order, or to check if a part from receiving is needed to fill shortages.

**product_id (INT) (NOT NULL) (This column references the PRODUCT table) : ** The product that has been ordered from the company.

**quantity (INT) (NOT NULL) : ** The amount of that product that has been ordered.

**status (VARCHAR) : ** The current status of the work order, this can only be 'CREATED', 'KITTED', 'BUILDING', 'COMPLETED', 'SHIPPED'

**date_created (DATETIME) : ** timestamp for when the work order was created.

**date_kitted (DATETIME) : ** timestamp for when the work order was kitted.

**date_building (DATETIME) : ** timestamp for when manufacturing started building the work order

**date_completed (DATETIME) : ** timestamp for when manufacturing completed building and ready to be shipped

**date_shipped (DATETIME) : ** timestamp for when the work order was fully shipped.

**ORDER_ITEMS**

DESCRIPTION:

This table will be the biggest one in our database, every item needed from every unique product ordered in every work order will have a row in this table. In order to make sense of this table, just like in the PRODUCT_BOM table, you will have to create queries that select for only the order ID that you want to make sense of this table. When a new work order is made for a product, all the parts from that product's PRODUCT_BOM rows are added to this table, with the number in the 'quantity' column of PRODUCT_BOM multiplied how many of that product was ordered (the number in the 'quantity' column of WORKORDERS) before added to this table. Essentially, this table will hold all all the kits from every work order ever made to the company.

COLUMNS:

**id (INT) (PRIMARY KEY) (AUTO INCREMENT) (NOT NULL) : ** a unique identifier for each row of the ORDER_ITEMS table, not important unless you need to remove one line of a kit.

**parts_id (INT) (NOT NULL) (This column references the PARTS table) : ** A reference to the information about each part.

**product_id (INT) (NOT NULL) (This column references the PRODUCTS table) : ** What part this work order is built to create.

**order_id (INT) (NOT NULL) (This column references the WORKORDERS table) : ** What work order this row is a part of.

**amount_needed (INT) (NOT NULL) : ** How many of the particular part is needed to complete the kit.

**amount_filled (INT) : ** How many of that particular part was actually put in the kit.

