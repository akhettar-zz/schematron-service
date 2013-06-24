Schematron and DTD Validation Service
======================================

REST Service performing DTD/Schema and Schematron Validation. Schema files and Schematron rules are stored in the file system. These files are loaded at the start of the service and refreshed from time to time depending what was configured in the property file.

Technology stack:

> Java
> REST Jersey
> Guice for DI
> Mockito
> JAXB

# To build eclipse project

> mvn eclipse:eclipse

