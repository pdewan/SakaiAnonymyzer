# SakaiAnonymyzer
anonymizes sakai projects
## Anon Usage
Anon anonymizes real names and onyens with sha256 hashes

If an input file is a folder and a zip file names "input.file.name+Backup.zip" does not exist, it backs up the input file by generating a zip file names "input.file.name+Backup.zip"

If an input file is a zip file, it unzips the zip file and set the input file to the unzipped folder

After anonymizing, it zips the anonymized version in a zip file called "input.file.name+A.zip", and deletes the input folder

#### Run:
To run Anon in command line, use the following command:

java -cp path.to.jar package.name.Anon mode list-of-paths...

#### Mode:
a: anonymize all paths

t: delete txt and html files, if not specified, the default is not deleting

d: delete all paths

Mode options should be entered as one word, e.x. act for anonymize, course mode, and delete txt and html files

#### Paths:
list-of-paths should be separated by spaces, surround a path with single quotes if the path contains spaces
<br/><br/>
## AnonFaker Usage
AnonFaker anonymizes real names and onyens or hashes with unique fake names, fake onyens will be the full fake name

It can also unanonymize an anonymized input, recovering the original names

If an input file is a folder and a zip file names "input.file.name+Backup.zip" does not exist, it backs up the input file by generating a zip file names "input.file.name+Backup.zip"

If an input file is a zip file, it unzips the zip file and set the input file to the unzipped folder

After (un)anonymizing, it zips the anonymized version in a zip file called "input.file.name+(U)A.zip", and deletes the input folder
#### Run:
To run Anon in command line, use the following command:

java -cp path.to.jar package.name.AnonFaker mode (path.to.name.map.csv) (path.to.fake.names.yml) list-of-paths...

#### Mode:
a: anonymize all paths

t: delete txt and html files, if not specified, the default is not deleting

u: unanonymize all paths

d: delete all paths

Mode options should be entered as one word, e.x. act for anonymize, course mode, and delete txt and html files

#### Paths:
path.to.name.map.csv and path.to.fake.names.yml are optional, the default will be ".\name map.csv" and ".\name.yml"
If path.to.name.map.csv does not exist, a new name map will be created at the specified path, which will cause anonymized results differ from previous results
If path.to.fake.names.yml does not exist, the program will crash

list-of-paths should be separated by spaces, surround a path with single quotes if the a path contains spaces