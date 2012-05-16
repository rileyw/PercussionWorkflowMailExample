#Percussion CM - Workflow email notification example

[Percussion Forum Cross-reference thread](http://forum.percussion.com/showthread.php?11090-github-java-Workflow-Email-Notification-Example)

You can install the extension by:

+ Compile the JAR
+ Copying the JAR to {PERCUSSION.PATH}/AppServer/server/rx/deploy/rxapp.ear/rxapp.war/WEB-INF/lib/
+ Update {PERCUSSION.PATH}/rxconfig/Workflow/rxworkflow.properties (Update through Workbench?)
	+ Add "CUSTOM_MAIL_CLASS=com.percussion.forum.WorkflowJavaxMailProgram" as a new line
+ Restart Application
