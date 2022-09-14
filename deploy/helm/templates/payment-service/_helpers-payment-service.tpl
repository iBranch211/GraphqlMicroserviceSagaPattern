{{- define "payment-service.selectorLabels" -}}
app: payment-service
release: {{ .Release.Name }}
{{- end }}

{{- define "payment-service.labels" -}}
chart: {{ include "app.chart" . }}
{{ include "payment-service.selectorLabels" . }}
heritage: {{ .Release.Service }}
app: payment-service
{{- end }}