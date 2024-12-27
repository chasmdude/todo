package com.secfix.todos.services;

import com.secfix.todos.database.models.CodeRepository;
import com.secfix.todos.enums.CodeRepositoryStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class CodeRepositoriesSyncCronJobTest {

    private static final Logger logger = LoggerFactory.getLogger(CodeRepositoriesSyncCronJobTest.class);

    @InjectMocks
    private CodeRepositoriesSyncCronJob codeRepositoriesSyncCronJob;

    @Mock
    private CodeRepositoriesManagementService codeRepositoriesManagementService;

    @Mock
    private GithubServiceWrapper githubServiceWrapper;

    private CodeRepository repo1;
    private CodeRepository repo2;
    private List<CodeRepository> activeRepos;

    @BeforeEach
    void setUp() {
        try (AutoCloseable ignored =  MockitoAnnotations.openMocks(this)) {
            repo1 = new CodeRepository();
            repo1.setName("Repo1");
            repo2 = new CodeRepository();
            repo2.setName("Repo2");
            activeRepos = Arrays.asList(repo1, repo2);
        } catch (Exception e) {
            logger.error("Error initializing mocks", e);
        }
    }

    @Test
    void testSyncGithubRepositories_Success() {
        when(codeRepositoriesManagementService.getActiveCodeRepositories()).thenReturn(activeRepos);
        when(githubServiceWrapper.getRepository(repo1)).thenReturn(repo1);
        when(githubServiceWrapper.getRepository(repo2)).thenReturn(repo2);

        codeRepositoriesSyncCronJob.syncGithubrepositories();

        verify(codeRepositoriesManagementService, times(1)).getActiveCodeRepositories();
        verify(githubServiceWrapper, times(1)).getRepository(repo1);
        verify(githubServiceWrapper, times(1)).getRepository(repo2);
        verify(codeRepositoriesManagementService, times(1)).batchUpdateCodeRepositories(activeRepos);
    }

    @Test
    void testSyncGithubRepositories_UpdateActiveRepos() {
        CodeRepository updatedRepo1 = new CodeRepository();
        updatedRepo1.setName("UpdatedRepo1");
        updatedRepo1.setStatus(CodeRepositoryStatus.INVALID);
        CodeRepository updatedRepo2 = new CodeRepository();
        updatedRepo2.setName("UpdatedRepo2");

        when(codeRepositoriesManagementService.getActiveCodeRepositories()).thenReturn(activeRepos);
        when(githubServiceWrapper.getRepository(repo1)).thenReturn(updatedRepo1);
        when(githubServiceWrapper.getRepository(repo2)).thenReturn(updatedRepo2);

        codeRepositoriesSyncCronJob.syncGithubrepositories();

        verify(codeRepositoriesManagementService, times(1)).getActiveCodeRepositories();
        verify(githubServiceWrapper, times(1)).getRepository(repo1);
        verify(githubServiceWrapper, times(1)).getRepository(repo2);
        verify(codeRepositoriesManagementService, times(1)).batchUpdateCodeRepositories(Arrays.asList(updatedRepo1, updatedRepo2));
    }
}