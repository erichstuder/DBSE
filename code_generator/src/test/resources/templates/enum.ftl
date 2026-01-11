<#list enums as enum>
pub enum ${enum.name} {
    <#list enum.members as name, value>
    ${name},
    </#list>
}

</#list>