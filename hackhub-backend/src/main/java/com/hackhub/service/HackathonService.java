package com.hackhub.service;

import com.hackhub.dto.request.CreateHackathonRequest;
import com.hackhub.dto.response.HackathonResponse;
import com.hackhub.dto.response.InscriptionResponse;

import java.util.List;

public interface HackathonService {
    List<HackathonResponse> getAllHackathons();
    List<HackathonResponse> searchHackathons(String query);
    HackathonResponse getById(Long id);
    HackathonResponse create(CreateHackathonRequest request, String email);
    HackathonResponse update(Long id, CreateHackathonRequest request, String email);
    void delete(Long id, String email);
    List<HackathonResponse> getMyHackathons(String email);
    List<InscriptionResponse> getEquipesInscritesByHackathon(Long hackathonId, String email);
    InscriptionResponse inscrireEquipe(Long hackathonId, String email);
    void desinscrireEquipe(Long hackathonId, String email);
}
