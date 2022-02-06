========================
-------READ ME----------
========================

All the source code, executable files, diagrams/charts and a video presentation is provided here in this zip file.

diagrams folder: Contains all the required projects communication and architectural design with operation of classes.

CheckoutSystem.wmv: A video file demonstrating the checkout system software and all its applications.


CheckoutSystem folder: Folder containing all the source code and prebuild of this software. The database file used here is store.db. For running this source code in your machine dont forget to add the jar file libraries in the classpath of the project. These files are under the lib folder.

lib folder: A folder with all the required libraries. This folder is also needed to run the executable jar file. To run this executable file without problem keep the lib folder and CheckoutSystem software at same folder or filepath.

CheckoutSystem.jar: This is the final build software with no errors known. This software starts with login where opens to two diffrent windows depending on cashier or manager.Here are some logIn info:
userid: 1 | pin: 1234 [Manager/Cashier]
userid: 2 | pin: 1234 [Cashier]
userid: 9 | pin: 1995 [Cashier]

Cashier Interface is made for sales. Item is scanned or entered which pops to phone number for loyality program. If phone number is not found in database then it automatically creates the database for that no. for getting track of credit points starting from 0. Cashier can checkout items from cash, card or cheque and after the transaction the inventory is automatically updated. It also verifies for check and cards. Receipt printer and Till are shown in the interface as well which works as in regular point of sales system.

Manager Interface provides you with control over the database. Features include:
. Add or delete accounts [Manager/Cashier/Both]
. Add, Update or delete Inventory
. Update or delete Order status of items.
. Approve Orders.
. Send Orders.
. Receive Orders.
. See purchase history
. Run Inventory which automatically gets the low inventory items from the inventory and adds them to the order list.

Here, some photos of database are also added for your convinence.

========================
-------Thank YOU--------
========================
