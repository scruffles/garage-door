$ ->
  ws = new WebSocket("ws://" + location.hostname + ":9000/diagnostics")

  ws.onmessage = (event) ->
    $('msg').text(event.data)

