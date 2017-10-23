{

AppsPWD=$1

lCURRENTDIR=`pwd`
export lCURRENTDIR
CurrentDate=`date '+%Y%m%d_%H%M%S'`
export CurrentDate

echo 'Deploying PLL Objects...'

<#list pllObjects as pllObject>
    <@createPllDeployment pllObject=pllObject/>
</#list>

<#macro createPllDeployment pllObject>
echo 'Backup ${pllObject.name}${pllObject.extension}'
cp $AU_TOP/resource/${pllObject.name}${pllObject.extension} $AU_TOP/resource/${pllObject.name}_$CurrentDate${pllObject.extension}

echo 'Copy ${pllObject.name}${pllObject.extension}'
cp EBS/resource/${pllObject.name}${pllObject.extension} $AU_TOP/resource/${pllObject.name}${pllObject.extension}

cd $AU_TOP/resource
echo 'Compile ${pllObject.name}${pllObject.extension}'
frmcmp_batch module=$AU_TOP/resource/${pllObject.name}${pllObject.extension} userid=apps/$AppsPWD output_file=$AU_TOP/resource/${pllObject.name}.plx module_type=LIBRARY batch=yes compile_all=special

cd $lCURRENTDIR
</#macro>

echo 'PLL deployment completed.'

} 2>&1 | tee deploy_pll.log