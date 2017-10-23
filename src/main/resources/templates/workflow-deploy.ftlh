{

AppsPWD=$1

lCURRENTDIR=`pwd`
export lCURRENTDIR
CurrentDate=`date '+%Y%m%d_%H%M%S'`
export CurrentDate
XNLS=$NLS_LANG

echo 'Deploying Workflow Objects...'

<#compress >
    <#list wfObjects as wfObject>
    NLS_LANG=${nlsLangProps[wfObject.language]}
    export NLS_LANG

    echo 'Uploading Workflow ${wfObject.language} ${wfObject.name}${wfObject.extension}'
    WFLOAD apps/$AppsPWD 0 Y UPLOAD EBS/wft/${wfObject.language}/${wfObject.name}${wfObject.extension}
    </#list>
</#compress>

NLS_LANG=$XNLS
export NLS_LANG

} 2>&1 | tee deploy_wf.log