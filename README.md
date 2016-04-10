# Elfinder Java Connector Support elFinder 2.x
"elFinder is an open-source file manager for web, written in JavaScript using jQuery UI".

Being a User interface, elFinder need support in order to develop connectors for various languages,
as well as community support to keep them.

In the Trustsystems group, we offer this fully compliance connector with the 2.x version, developed using technology
which are known on the market.

## Technologies

* Java Development Kit 7 supporting NIO2.
* Spring Framework

## List of some supported features

* All operations with files and folders on a remote server (copy, move, upload, create folder/file, rename, etc.)
* Local file system, and total code infraestructure to support web drivers e.g: Dropobox.
* Archives create/extract (zip, tar, tgz)

## Supported Commands

* DimCommand
* ArchiveCommand
* DuplicateCommand
* ExtractCommand
* FileCommand
* GetCommand
* LsCommand
* MkdirCommand
* MkFileCommand
* OpenCommand
* ParentsComman
* PutCommand
* RenameCommand
* RmCommand
* SearchCommand
* SizeCommand
* TmbCommand
* TreeCommand
* UploadCommand
* PutCommand

## Sample usage

You do not need to modify your web.xml file.
You only need to put this library in your classpath or in the maven dependencies.
We are using the WebApplicationInitializer SpringFramework class that is based on Java6 ServletContainerInitializer.

 * https://github.com/trustsystems/elfinder-demo

## Configuration

### ElFinder Configuration XML Example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elfinder-configuration 
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://www.trustsystems.com.br/elfinder" xsi:schemaLocation="http://www.trustsystems.com.br/elfinder http://www.trustsystems.com.br/elfinder/elfinder-configuration.xsd">

    <thumbnail> 
        <width>80</width> 
    </thumbnail> 

    <volume> 
        <source>filesystem</source> 
        <alias>My Documents</alias> 
        <path>/home/thiagocosta/Documents</path> 
        <locale>pt_BR</locale> 
        <constraint> 
            <locked>false</locked> 
            <readable>true</readable> 
            <writable>true</writable> 
        </constraint> 
    </volume>

    <volume> 
        <source>filesystem</source> 
        <alias>My Pictures</alias> 
        <path>/home/thiagocosta/Pictures</path> 
        <locale>pt_BR</locale> 
        <constraint> 
            <locked>true</locked> 
            <readable>true</readable> 
            <writable>false</writable> 
        </constraint> 
    </volume>

</elfinder-configuration>
```

### Details

```
This element must occur only once.
<thumbnail> This element represents thumbnail.
    <width> This element defines the width of the thumbnail images. Minimum is 80.

This element can occurs one or more times.
<volume> This element represents the volume.
    <source/> This element defines the source of the volume. [filesystem, dropbox, googledrive, onedrive] only filesystem is supported in the moment.
    <alias/> This element defines an virtual name for the volume.
    <path/> This element defines the path of the unit to be shared.
    <default/> (Optional) This element is the first volume to be loaded. In the event of multiple occurrences the first will be selected.
    <locale/> This element defines the locale of the volume for future indexing proposals.
    <constraint> This element is the restriction of operations that may be applied in specific volume.
        <locked/> This element defines the total directory lock (Usually applies to groups).
        <readable/> This element defines the possibility of reading the file.
        <writable/> This element defines the possibility of writing in the file.
```

### Usage

ElFinder Java Connector will try to load and read the elfinder-configuration.xml in the filesystem. So it is necessary to create this configuration file inside of a folder named "elfinder" in the user.home.

Authors
-------

 * Wenderson Ferreira de Souza <wenderson@trustsystems.com.br>
 * Thiago Gutenberg Carvalho da Costa <thiaguten@gmail.com>

License
-------

elFinder Java Connector is issued under a 3-clauses BSD license.

<pre>
Copyright (C) 2015 Trustsystems Desenvolvimento de Sistemas, LTDA.

Redistribution and use in source and binary forms, with or without modification,
are permitted provided that the following conditions are met:

* 1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer.

* 2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution.

* 3. Neither the name of the Trustsystems Desenvolvimento de Sistemas, LTDA. nor the names of its contributors
   may be used to endorse or promote products derived from this software without
   specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
OF THE POSSIBILITY OF SUCH DAMAGE.
</pre>

Your code contribution will be very welcome
Be free
