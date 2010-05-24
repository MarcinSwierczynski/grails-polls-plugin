<div class="poll_message">
    <g:if test="${hasVoted}">
        You've already voted in this poll!
    </g:if>
    <g:else>
        Thanks for your vote!
    </g:else>
</div>
<polls:pollResults id="${poll.id}" type="pie3D" width="250" height="100" colors="['ff0000', '00ff00', '0000ff']"/>