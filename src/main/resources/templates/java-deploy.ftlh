{

AppsPWD=$1
lSID=`echo $TWO_TASK`
lTNS=`tnsping $TWO_TASK | grep HOST`
lHOST=`echo $lTNS | awk -F'HOST=' '{print $2}' | awk -F')' '{print $1}'`
lPORT=`echo $lTNS | awk -F'PORT=' '{print $2}' | awk -F')' '{print $1}'`
lCURRENTDIR=`pwd`
export lCURRENTDIR

echo 'Copy JAVA_TOP Files...'
cp -R EBS/java/* $JAVA_TOP/

<#compress>
    <#if oaMediaExists == "yes">
    echo 'Copy OA_MEDIA files...'
    cp -R EBS/OA_MEDIA/* $OA_MEDIA/
    </#if>
</#compress>

<#compress>
    <#if oaHtmlExists == "yes">
    echo 'Copy OA_HTML files...'
    cp -R EBS/OA_HTML/* $OA_HTML/

    echo 'Compiling Jsp Files...'
    cd $OA_HTML
        <#list javaObjects as javaObject>
            <#assign type = javaObject.extension?upper_case[1..]/>
            <#assign fullPath = javaObject.path + javaObject.name + javaObject.extension/>

            <#if type == "JSP">
                <#if javaObject.path?contains("OA_HTML")>
                $FND_TOP/patch/115/bin/ojspCompile.pl --compile -s '${javaObject.name}${javaObject.extension}' -p 2
                </#if>
            </#if>
        </#list>
    cd $lCURRENTDIR
    </#if>
</#compress>

echo 'Compiling Java Files...'

<#compress>
    <#list javaObjects as javaObject>
        <#assign type = javaObject.extension?upper_case[1..]/>

        <#if type == "JAVA">
        find $JAVA_TOP/${javaObject.path}*.java -exec javac {} \;
        </#if>
    </#list>
</#compress>

echo 'Importing Xml and Xlf Files...'

<#compress>
    <#list javaObjects as javaObject>
        <#assign type = javaObject.extension?upper_case[1..]/>
        <#assign fullPath = javaObject.path + javaObject.name + javaObject.extension/>

        <#if type == "XLF">
            <#if javaObject.path?contains("localization")>
            echo 'Importing Localization: ${fullPath}'
            java oracle.jrad.tools.trans.imp.XLIFFImporter -username apps -password $AppsPWD -dbconnection "(description = (address_list = (address = (community = tcp.world)(protocol =tcp)(host = $lHOST)(port = $lPORT)))(connect_data = (sid=$lSID)))" $lCURRENTDIR/EBS/${fullPath}
            </#if>
        <#elseif type == "XML">
            <#if javaObject.path?contains("personalization")>
            echo 'Importing Personalization: ${fullPath}'
            java oracle.jrad.tools.xml.importer.XMLImporter $lCURRENTDIR/EBS/${fullPath} -username apps -password $AppsPWD -rootdir $lCURRENTDIR/EBS/java/personalization -dbconnection "(description = (address_list = (address = (community = tcp.world)(protocol =tcp)(host = $lHOST)(port = $lPORT)))(connect_data = (sid=$lSID)))"
            <#elseif javaObject.path?contains("webui")>
            echo 'Importing Page or Region: ${fullPath}'
            java oracle.jrad.tools.xml.importer.XMLImporter $JAVA_TOP/${fullPath} -username apps -password $AppsPWD -rootdir $JAVA_TOP -dbconnection "(description = (address_list = (address = (community = tcp.world)(protocol =tcp)(host = $lHOST)(port = $lPORT)))(connect_data = (sid=$lSID)))"
            </#if>
        </#if>
    </#list>

</#compress>

} 2>&1 | tee deploy_java.log