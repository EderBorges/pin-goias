package gov.goias.sistema.negocio;

import gov.goias.sistema.entidades.Aluno;
import gov.goias.sistema.exception.InfraException;
import gov.goias.sistema.repositories.AlunoRepository;
import javaslang.collection.HashMap;
import javaslang.collection.Map;
import javaslang.control.Try;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Named
public class AlunoService {
    @Inject
    private AlunoRepository alunoRepository;

    public Optional<Aluno> consultar(Integer id) {
        return Try.of(() -> alunoRepository.findById(id)).onFailure(e -> new InfraException(e)).get();
    }

    public Optional<Aluno> alterar(Aluno aluno) {
        return Try.of(() -> alunoRepository.update(aluno)).onFailure(e -> new InfraException(e)).get();
    }

    public Optional<Aluno> salvar(Aluno aluno) {
        return Try.of(() -> alunoRepository.save(aluno)).onFailure(e -> new InfraException(e)).get();
    }


    public void excluir(Integer id) {
        try {
            alunoRepository.findById(id).ifPresent(a -> alunoRepository.delete(a));
        } catch (Exception e) {
            throw new InfraException(e);
        }
    }

    public Integer obtemQuantidadeTotalRegistros() {
        return alunoRepository.obtemQuantidadeTotalRegistros();
    }

    public Optional<List<Aluno>> listarAlunos(Integer offset, Integer limit) {
        return alunoRepository.listarAlunos(offset, limit);
    }

    public void armazenaArquivo(Integer idAluno, String nomeArquivo, InputStream arquivo) {
        String separator = System.getProperty("file.separator", "/");

        File diretorioDestino = new File("/tmp/pin-goias" + separator + idAluno );
        diretorioDestino.mkdirs();

        try {

            Files.copy(arquivo, new File(diretorioDestino.getAbsolutePath(), nomeArquivo).toPath(), StandardCopyOption.REPLACE_EXISTING);

        } catch (Exception e) {
            throw new InfraException(e);
        }

    }

}
