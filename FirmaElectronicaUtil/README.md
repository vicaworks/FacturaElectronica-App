# UTILITARIO

> Firma digital

## Clase principal

```
	La clase principal para la ejecución de la firma es : com/mako/util/firma/XAdESBESSignature.java
```

### Requisitos previos

1. Tener almacenado en el disco la firma digital, o en la base de datos
2. Clave de la firma digital
3. Tener almacenado el XML en el disco, un String, o en la base de datos

### Métodos 

**AAAA**

```
public static void firmarFacade(String xmlPath, String pathSignature, String passSignature, 
            String pathOut, String nameFileOut) throws CertificateException, FirmaElectronicaException
``` 

|parámetro|descripción|
|---------|-----------|
| xmlPath | Path en el disco del archivo xml a firmar    |
| pathSignature | path en el disco de la fima digital    |
| passSignature | clave de la firma digital    |
| pathOut |  path del xml ya firmado   |
| nameFileOut |  nombre del xml    |

| #| Historia Usuario |
|--|------------------|
| 1|Especificación de la historia de usuario|
| 1|Especificación de la historia de usuario|