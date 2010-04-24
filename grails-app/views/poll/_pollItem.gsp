<div class="poll_item" id="poll${poll?.id}">
  <p class="poll_question">
    ${poll?.question}
  </p>
  <ul class="poll_answers">
    <g:each in="${poll.answers}" var="answer">
    <li class="poll_answer">
      <p>
        ${answer?.content}
      </p>
    </li>
    </g:each>
  </ul>
</div>