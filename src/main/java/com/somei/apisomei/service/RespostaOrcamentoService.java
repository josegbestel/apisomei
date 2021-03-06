package com.somei.apisomei.service;

import com.somei.apisomei.exception.DomainException;
import com.somei.apisomei.exception.NotFoundException;
import com.somei.apisomei.model.Agenda;
import com.somei.apisomei.model.Servico;
import com.somei.apisomei.model.RespostaOrcamento;
import com.somei.apisomei.model.enums.StatusServico;
import com.somei.apisomei.model.representationModel.RespostaOrcamentoModel;
import com.somei.apisomei.model.representationModel.RespostaOrcamentoNovoModel;
import com.somei.apisomei.repository.AgendaRepository;
import com.somei.apisomei.repository.ServicoRepository;
import com.somei.apisomei.repository.ProfissionalRepository;
import com.somei.apisomei.repository.RespostaOrcamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RespostaOrcamentoService {

    @Autowired
    RespostaOrcamentoRepository respostaOrcamentoRepository;

    @Autowired
    AgendaRepository agendaRepository;

    @Autowired
    ProfissionalRepository profissionalRepository;

    @Autowired
    ServicoRepository servicoRepository;

    //Ler por id
    public RespostaOrcamentoModel read(long id){
        RespostaOrcamento resposta = respostaOrcamentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Resposta não localizada."));

        return RespostaOrcamentoModel.toModel(resposta);
    }

    //Ler por profissional
    public List<RespostaOrcamentoModel> readByProfissional(long id){
        if(!profissionalRepository.existsById(id)){
            throw new NotFoundException("Profissional não localizado");
        }
        List<RespostaOrcamento> respostas = respostaOrcamentoRepository.findByProfissionalIdOrderByDtRespostaDesc(id)
                .orElseThrow(() -> new NotFoundException("Profissional não possui respostas vinculadas."));

        //Filtrar apenas não respondidos
        respostas = respostas.stream().filter(r -> r.getDtResposta() == null).collect(Collectors.toList());

        if(respostas.size() == 0)
            throw new NotFoundException("Profissional não possui respostas vinculadas.");

        List<RespostaOrcamentoModel> models = new ArrayList<>();
        respostas.forEach(r -> models.add(RespostaOrcamentoModel.toModel(r)));

        return models;
    }

    //Responder
    public RespostaOrcamento updateResposta(long idResposta, RespostaOrcamentoNovoModel respostaModel){
        RespostaOrcamento respostaOrcamento = respostaOrcamentoRepository.findById(idResposta)
                .orElseThrow(() -> new NotFoundException("Resposta não localizada"));

        //Verificar se já foi respondida
        if(respostaOrcamento.getDtResposta() != null)
            throw new DomainException("Esta solicitação já foi respondida");

        Agenda agenda = agendaRepository.findById(respostaModel.getAgendaId())
                .orElseThrow(() -> new NotFoundException("Agenda não localizada"));

        //Definir no orçamento status respondida
        Servico servico = servicoRepository.findById(respostaModel.getServicoId())
                .orElseThrow(() -> new NotFoundException("Serviço não localizado"));

        //Verificar se existe esta resposta no serviço
        if(servico.getRespostas() != null){
            boolean respostaInOrcamento = false;
            for (RespostaOrcamento resposta : servico.getRespostas()) {
                System.out.println("Entrou no for");
                if (resposta.getId() == idResposta)
                    respostaInOrcamento = true;
            }
            if(respostaInOrcamento)
                throw new DomainException("Esta resposta não pertence a este serviço");

        }

        //Alterar status do orcamento como RESPONDIDO
        servico.setStatus(StatusServico.RESPONDIDO);
        servicoRepository.save(servico);

        //Definir agenda como escolhida
        respostaOrcamento.setAgenda(agenda);

        //Atualizar resposta com as informações
        respostaOrcamento.setValor(respostaModel.getValor());
        respostaOrcamento.setObservacao(respostaModel.getObservacao());
        respostaOrcamento.setDtResposta(LocalDateTime.now());
        return respostaOrcamentoRepository.save(respostaOrcamento);
    }


    public Servico obterOrcamento(long respostaId){
        RespostaOrcamento orcamento = respostaOrcamentoRepository.findById(respostaId)
                .orElseThrow(() -> new NotFoundException("Orcamento não localizado"));

        return orcamento.getOcamentoClean();
    }


}
