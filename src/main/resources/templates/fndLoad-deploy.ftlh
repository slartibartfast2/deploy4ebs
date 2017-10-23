{

AppsPWD=$1

XNLS=$NLS_LANG
NLS_LANG=American_America.AL32UTF8
export NLS_LANG

echo 'Deploying LDT Objects...'

<#list fndLoadObjects as fndLoadObject>
    <#compress>
    echo 'Uploading ${fndLoadObject.language} ${fndLoadObject.type}... File Name: ${fndLoadObject.name}${fndLoadObject.extension}'
        <#if fndLoadObject.language == "US">
        FNDLOAD apps/$AppsPWD 0 Y UPLOAD ${fndLoadObject.applTopName}/patch/115/import/${fndLoadObject.lctFile} EBS/${fndLoadObject.path}${fndLoadObject.name}${fndLoadObject.extension}
        <#else>
        FNDLOAD apps/$AppsPWD 0 Y UPLOAD ${fndLoadObject.applTopName}/patch/115/import/${fndLoadObject.lctFile} EBS/${fndLoadObject.path}${fndLoadObject.name}${fndLoadObject.extension} UPLOAD_MODE=NLS
        </#if>
    </#compress>

</#list>

echo 'LDT deployment completed.'

NLS_LANG=$XNLS
export NLS_LANG

} 2>&1 | tee deploy_ldt.log