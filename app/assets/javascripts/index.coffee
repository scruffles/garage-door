$ ->
  ws = new WebSocket("ws://" + location.hostname + ":9000/status")

  ws.onmessage = (event) ->
    $('.status').text(event.data)
    if (event.data == 'open')
      $('button').text('close').removeClass('disabled')
    else if (event.data == 'closed')
      $('button').text('open').removeClass('disabled')
    else
      $('button').text('please wait...').addClass('disabled')

  ws.onclose = ->
    console.log("Socket closed")

  ws.onopen = ->
    ws.send("request-status")

  $('button').click (e) ->
    e.preventDefault()
    ws.send "trigger-door"
