package br.com.contador_presencial.data.mapper

import br.com.contador_presencial.data.local.entity.AutorEntity
import br.com.contador_presencial.data.local.entity.EnderecoEntity
import br.com.contador_presencial.data.local.entity.HistoricoVisitasEntity
import br.com.contador_presencial.domain.model.Autor
import br.com.contador_presencial.domain.model.Endereco
import br.com.contador_presencial.domain.model.HistoricoVisitas

fun AutorEntity.toDomain(): Autor =
    Autor(
        id = id,
        periodicidade = periodicidade,
    )

fun Autor.toEntity(): AutorEntity =
    AutorEntity(
        id = id,
        periodicidade = periodicidade,
    )

fun EnderecoEntity.toDomain(): Endereco =
    Endereco(
        id = id,
        autorId = autorId,
        rua = rua,
        numero = numero,
        complemento = complemento,
        bairro = bairro,
        cidade = cidade,
        uf = uf,
        latitude = latitude,
        longitude = longitude,
    )

fun Endereco.toEntity(): EnderecoEntity =
    EnderecoEntity(
        id = id,
        autorId = autorId,
        rua = rua,
        numero = numero,
        complemento = complemento,
        bairro = bairro,
        cidade = cidade,
        uf = uf,
        latitude = latitude,
        longitude = longitude,
    )

fun HistoricoVisitasEntity.toDomain(): HistoricoVisitas =
    HistoricoVisitas(
        id = id,
        enderecoId = enderecoId,
        horas = horas,
    )

fun HistoricoVisitas.toEntity(): HistoricoVisitasEntity =
    HistoricoVisitasEntity(
        id = id,
        enderecoId = enderecoId,
        horas = horas,
    )
