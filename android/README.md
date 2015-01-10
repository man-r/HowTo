CREATE ANDROID PROJECT FROM COMMAND LINE
=====

RUN THE FOLLWOING COMMAND FROM TOOL DIRECTORY

Before we can create our project we need to find out wich targets are available in our installation:
<code>./android list targets</code>


Then use the <strong>create project</strong> command
<code>./android create project --target <target_ID> --name <your_project_name> --path path/to/your/project --activity <your_activity_name> --package <your_package_namespace></code>


example

<code>./android create project --target 1 --name TestApp --path /home/myself/android/myapp --activity MyMainActivity --package com.example.testapp</code>
