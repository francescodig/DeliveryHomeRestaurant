<section class="message-section">
    <#if messages??>
        <#list messages?keys as type>
            <#list messages[type] as msg>
                <div class="alert alert-${type}">${msg}</div>
            </#list>
        </#list>
    </#if>
</section>
