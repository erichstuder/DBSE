<#list packages as package>
// Package: ${package.name}

<#list package.enums as enum>
pub enum ${enum.name} {
    <#list enum.members as member>
    ${member.name},
    </#list>
}

</#list>
</#list>