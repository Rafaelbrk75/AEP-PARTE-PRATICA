package br.edu.unicesumar.aep_parte2.mapper;

import br.edu.unicesumar.aep_parte2.domain.dto.AnexoResponse;
import br.edu.unicesumar.aep_parte2.domain.dto.SolicitacaoRequest;
import br.edu.unicesumar.aep_parte2.domain.dto.SolicitacaoResponse;
import br.edu.unicesumar.aep_parte2.domain.entity.AnexoModel;
import br.edu.unicesumar.aep_parte2.domain.entity.SolicitacaoModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring", uses = HistoricoStatusMapper.class)
public interface SolicitacaoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "protocolo", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "atrasado", ignore = true)
    @Mapping(target = "prazoLimite", ignore = true)
    @Mapping(target = "justificativaAtraso", ignore = true)
    @Mapping(target = "solicitante", ignore = true)
    @Mapping(target = "atendenteResponsavel", ignore = true)
    @Mapping(target = "historico", ignore = true)
    @Mapping(target = "anexos", ignore = true)
    @Mapping(target = "statusAtual", ignore = true)
    SolicitacaoModel toEntity(SolicitacaoRequest request);

    @Mapping(target = "nomeSolicitante", expression = "java(nomeSolicitante(model))")
    @Mapping(source = "atendenteResponsavel.nome", target = "nomeAtendenteResponsavel")
    @Mapping(target = "anexos", expression = "java(toAnexoResponseList(model.getAnexos()))")
    SolicitacaoResponse toResponse(SolicitacaoModel model);

    List<SolicitacaoResponse> toResponseList(List<SolicitacaoModel> models);

    default String nomeSolicitante(SolicitacaoModel model) {
        if (Boolean.TRUE.equals(model.getAnonima())) {
            return "Solicitante anonimo";
        }

        if (model.getSolicitante() == null) {
            return null;
        }

        return model.getSolicitante().getNome();
    }

    default List<AnexoResponse> toAnexoResponseList(List<AnexoModel> anexos) {
        if (anexos == null || anexos.isEmpty()) {
            return Collections.emptyList();
        }

        return anexos.stream().map(this::toAnexoResponse).toList();
    }

    default AnexoResponse toAnexoResponse(AnexoModel anexo) {
        return new AnexoResponse(
                anexo.getId(),
                anexo.getSolicitacao() == null ? null : anexo.getSolicitacao().getId(),
                anexo.getNomeArquivo(),
                resolverUrlAnexo(anexo),
                anexo.getTipoConteudo(),
                anexo.getAutor() == null ? null : anexo.getAutor().getNome(),
                anexo.getDataEnvio()
        );
    }

    default String resolverUrlAnexo(AnexoModel anexo) {
        String url = anexo.getUrl();
        if (url == null || url.startsWith("http://") || url.startsWith("https://") || url.startsWith("/")) {
            return url;
        }
        return "/api/anexos/" + anexo.getId() + "/arquivo";
    }
}
