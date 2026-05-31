package br.edu.unicesumar.aep_parte2.mapper;

import br.edu.unicesumar.aep_parte2.domain.dto.SolicitacaoRequest;
import br.edu.unicesumar.aep_parte2.domain.dto.SolicitacaoResponse;
import br.edu.unicesumar.aep_parte2.domain.entity.SolicitacaoModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SolicitacaoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "protocolo", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "atrasado", ignore = true)
    @Mapping(target = "solicitante", ignore = true)
    @Mapping(target = "historico", ignore = true)
    @Mapping(target = "statusAtual", ignore = true)
    SolicitacaoModel toEntity(SolicitacaoRequest request);

    // Entity → Response
    // campos com nomes diferentes precisam de @Mapping explícito
    @Mapping(source = "solicitante.nome", target = "nomeSolicitante")
    SolicitacaoResponse toResponse(SolicitacaoModel model);

    List<SolicitacaoResponse> toResponseList(List<SolicitacaoModel> models);
}
