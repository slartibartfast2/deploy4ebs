{

AppsPWD=$1

lCURRENTDIR=`pwd`
export lCURRENTDIR
CurrentDate=`date '+%Y%m%d_%H%M%S'`
export CurrentDate

echo 'Deploying FMB Objects...'

<#compress >
    <#list formObjects as formObject>

    echo 'Backup ${formObject.language} ${formObject.name}${formObject.extension}'
    cp $AU_TOP/forms/${formObject.language}/${formObject.name}${formObject.extension} $AU_TOP/forms/${formObject.language}/${formObject.name}_$CurrentDate${formObject.extension}

    echo 'Copy ${formObject.language} ${formObject.name}${formObject.extension}'
    cp EBS/fmb/${formObject.language}/${formObject.name}${formObject.extension} $AU_TOP/forms/${formObject.language}/${formObject.name}${formObject.extension}

    cd $AU_TOP/forms/${formObject.language}
    echo 'Compile ${formObject.language} ${formObject.name}${formObject.extension}'
    frmcmp_batch module=$AU_TOP/forms/${formObject.language}/${formObject.name}${formObject.extension} userid=apps/$AppsPWD output_file=$${customTop}/forms/${formObject.language}/${formObject.name}.fmx
    cd $lCURRENTDIR

    </#list>
</#compress>

echo 'FMB deployment completed.'

} 2>&1 | tee deploy_forms.log