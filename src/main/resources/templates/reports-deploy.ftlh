{

AppsPWD=$1

lSID=`echo $TWO_TASK`
lTNS=`tnsping $TWO_TASK | grep HOST`
lHOST=`echo $lTNS | awk -F'HOST=' '{print $2}' | awk -F')' '{print $1}'`
lPORT=`echo $lTNS | awk -F'PORT=' '{print $2}' | awk -F')' '{print $1}'`

lCURRENTDIR=`pwd`
export lCURRENTDIR
CurrentDate=`date '+%Y%m%d_%H%M%S'`
export CurrentDate

echo 'Deploying Reports...'

<#compress>
    <#list reportObjects as reportObject>
        <#assign type = reportObject.extension?upper_case[1..]/>
        <#assign nlsLang = nlsLangProps[reportObject.language]/>
        <#assign fullName = reportObject.name + reportObject.extension/>

        <#if type == "RDF">
        echo 'Backup ${reportObject.language} ${fullName}'
        cp $${customTop}/reports/${reportObject.language}/${fullName} $${customTop}/reports/${reportObject.language}/${reportObject.name}_$CurrentDate${reportObject.extension}

        echo 'Copy RDF: ${reportObject.language} ${fullName}'
        cp EBS/reports/rdf/${reportObject.language}/${fullName} $${customTop}/reports/${reportObject.language}/${fullName}
        <#elseif type == "RTF">
        echo 'Importing RTF: ${fullName}'
        java oracle.apps.xdo.oa.util.XDOLoader UPLOAD -DB_USERNAME apps -DB_PASSWORD $AppsPWD -JDBC_CONNECTION "(DESCRIPTION= (ADDRESS=(PROTOCOL=tcp)(HOST=$lHOST)(PORT=$lPORT)) (CONNECT_DATA= (SID=$lSID)))" -LOB_TYPE TEMPLATE -LOB_CODE ${reportObject.name} -XDO_FILE_TYPE ${type} -FILE_NAME EBS/${reportObject.path}${fullName} -APPS_SHORT_NAME ${appShortName} -NLS_LANG $NLS_LANG -LANGUAGE ${reportObject.language} -TERRITORY ${territory} -LOG_FILE ${reportObject.name}.log
        <#elseif type == "XDO">
        echo 'Importing XDO: ${fullName}'
        java oracle.apps.xdo.oa.util.XDOLoader UPLOAD -DB_USERNAME apps -DB_PASSWORD $AppsPWD -JDBC_CONNECTION "(DESCRIPTION= (ADDRESS=(PROTOCOL=tcp)(HOST=$lHOST)(PORT=$lPORT)) (CONNECT_DATA= (SID=$lSID)))" -LOB_TYPE DATA_TEMPLATE -LOB_CODE ${reportObject.name} -XDO_FILE_TYPE ${type} -FILE_NAME EBS/${reportObject.path}${fullName} -APPS_SHORT_NAME ${appShortName} -NLS_LANG $NLS_LANG -LANGUAGE ${reportObject.language} -TERRITORY ${territory} -LOG_FILE ${reportObject.name}.log
        </#if>

    </#list>
</#compress>

} 2>&1 | tee deploy_reports.log