{{- define "order-service.selectorLabels" -}}
app: order-service
release: {{ .Release.Name }}
{{- end }}

{{- define "order-service.labels" -}}
chart: {{ include "app.chart" . }}
{{ include "order-service.selectorLabels" . }}
heritage: {{ .Release.Service }}
app: order-service
{{- end }}