# How to call as an executable JAR (for Local Directory)
java -jar [JAR-NAME] LOCAL [OUTPUT-DIRECTORY] [INPUT-DIRECTORY]


# How to call as an executable JAR (for SVN)
java -jar [JAR-NAME] CLEARCASE|GITHUB [LABEL-NAME] [OUTPUT-DIRECTORY]

# How to call in Java (for Local Directory)

Before call from Java you must create a file named deploy4ebs.properties otherwise you will get file not found exception. You can find sample propery file under project directory.

    // Create EBS Deployment Package From Local Directory.
    long startTime = System.currentTimeMillis();
        
        
    SvnType svnType = SvnType.LOCAL;
        
    String sourcePath = "." + IOUtils.DIR_SEPARATOR + "test-input" + IOUtils.DIR_SEPARATOR + "erpDeployment";
    String outputPath = "." + IOUtils.DIR_SEPARATOR + "test-output";

    Deployment deployment = new LocalDeployment(sourcePath);
    deployment.create(outputPath);
        
    long endTime = System.currentTimeMillis();
    NumberFormat formatter = new DecimalFormat("#0.00000");
    System.out.println("Execution time is " + formatter.format((endTime - startTime) / 1000d) + " seconds");
    
# How to call in Java (for SVN)

    // Create EBS Deployment Package From SVN.
    long startTime = System.currentTimeMillis();
        
    SvnType svnType = SvnType.CLEARCASE;
        
    String labelName = "TO_UCCSTEST";
    String outputPath = "." + IOUtils.DIR_SEPARATOR + "test-output";

    Deployment deployment = new SvnDeployment(svnType, labelName);
    deployment.create(outputPath);

    long endTime = System.currentTimeMillis();
    NumberFormat formatter = new DecimalFormat("#0.00000");
    System.out.println("Execution time is " + formatter.format((endTime - startTime) / 1000d) + " seconds");*/
    
# Input Format
  You should create a directory named EBS and copy your application objects under this folder. You can find sample input directory format under INPUT-TEMPLATE.
