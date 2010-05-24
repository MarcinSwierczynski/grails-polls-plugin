<div class="poll_message">
    <g:if test="${hasVoted}">
        <g:message code="poll.plugin.vote.repeated" />
    </g:if>
    <g:else>
        <g:message code="poll.plugin.vote.saved" />
    </g:else>
</div>
<polls:pollResults id="${poll.id}" type="pie3D" width="250" height="100" colors="['ff0000', '00ff00', '0000ff']"/>