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

1. Genera un archivo en el disco ya firmado

```Java
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

2. Utiliza Byte en todos los parámetros y retorna un byte con el xml firmado

```Java
public static byte[] firmarFacade(byte[] xmlToSign,byte[] penSignature,String passSignature)throws CertificateException, FirmaElectronicaException
```
|parámetro|descripción|
|---------|-----------|
| xmlToSign | byte[] del xml a firmar    |
| penSignature | byte[] del archivo de la firma digital    |
| passSignature | Clave de la firma digital    |


