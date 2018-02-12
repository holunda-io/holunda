## Tenant-specific DMN tables

This example shows how to...

* do tenant-specific deployments for DMN tables (see `META-INF/processes.xml`)
* automatically set the tenant ID from the logged-in user on the started process instance (see class `CamundaConfiguration` ff.)
* call the respective DMN table from a process execution (see rule task `Determine digit as word` ff.)

When you start **Digit to Word Process** you will be able to enter a digit (as a number) and by means of a DMN
table the word for this digit is determined and stored as process variable `digit_word` (just check the history in Cockpit). 
Depending on different users, different tenant-specific versions of the DMN table are used. Just try these users 
and also checkout their tenant configuration in Camunda Admin:

* admin
* james
* klaus
* francois

Pssst: the passwords for these users are equal to their usernames ;-x
