{

AppsPWD=$1

lCURRENTDIR=`pwd`
export lCURRENTDIR
CurrentDate=`date '+%Y%m%d_%H%M%S'`
export CurrentDate

echo 'Deploying CUSTOM-TOP...'

<#list customTopObjects as customTopObject>

    <#compress>
    echo 'Deploying ${customTopObject.path}${customTopObject.name}${customTopObject.extension}'

    echo 'Create backup file'
    cp -P $${customTopObject.path}${customTopObject.name}${customTopObject.extension} $${customTopObject.path}${customTopObject.name}_$CurrentDate${customTopObject.extension}

    echo 'Create ${customTopObject.path} folder if not exists'
    mkdir -p $${customTopObject.path}

    echo 'Copy new file'
    cp -P EBS/${customTopObject.path}${customTopObject.name}${customTopObject.extension} $${customTopObject.path}${customTopObject.name}${customTopObject.extension}

    cd $lCURRENTDIR
    </#compress>

</#list>

echo 'CUSTOM-TOP deployment completed.'

} 2>&1 | tee deploy_customTop.log