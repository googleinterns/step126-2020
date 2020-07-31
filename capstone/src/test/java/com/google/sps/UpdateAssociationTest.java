import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.cloud.language.v1.AnalyzeEntitySentimentRequest;
import com.google.cloud.language.v1.Entity;
import com.google.cloud.language.v1.EntityMention;
import com.google.cloud.language.v1.LanguageServiceClient;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Before;

public class UpdateAssociationTest {

  private LanguageServiceClient mockNLP;

  @Before
  public void setup() {
    setupMockNLP();
  }

  private void setupMockNLP() {
    mockNLP = mock(LanguageServiceClient.class);

    when(mockNLP.analyzeEntitySentiment(any(AnalyzeEntitySentimentRequest.class)).getEntitiesList())
        .thenReturn(new ArrayList<Entity>())
        .thenReturn(Arrays.asList(getEntityOne(), getEntityThree()))
        .thenReturn(
            Arrays.asList(getEntityOne(), getEntityTwo(), getEntityThree())); // any subsequent call
  }

  private Entity getEntityOne() {
    Entity res = mock(Entity.class);
    when(res.getName()).thenReturn("test1");
    when(res.getMentionsList()).thenReturn(new ArrayList<EntityMention>());
    return res;
  }

  private Entity getEntityTwo() {
    Entity res = mock(Entity.class);
    when(res.getName()).thenReturn("test2");
    when(res.getMentionsList())
        .thenReturn(Arrays.asList(getMentionOne(), getMentionTwo(), getMentionFour()));
    return res;
  }

  private Entity getEntityThree() {
    Entity res = mock(Entity.class);
    when(res.getName()).thenReturn("test3");
    when(res.getMentionsList())
        .thenReturn(Arrays.asList(getMentionOne(), getMentionThree(), getMentionFour()));
    return res;
  }

  private EntityMention getMentionOne() {
    EntityMention res = mock(EntityMention.class);
    when(res.getSentiment().getMagnitude()).thenReturn(1.0f);
    when(res.getSentiment().getScore()).thenReturn(-0.5f);
    return res;
  }

  private EntityMention getMentionTwo() {
    EntityMention res = mock(EntityMention.class);
    when(res.getSentiment().getMagnitude()).thenReturn(0.3f);
    when(res.getSentiment().getScore()).thenReturn(0.7f);
    return res;
  }

  private EntityMention getMentionThree() {
    EntityMention res = mock(EntityMention.class);
    when(res.getSentiment().getMagnitude()).thenReturn(0.2f);
    when(res.getSentiment().getScore()).thenReturn(-0.2f);
    return res;
  }

  private EntityMention getMentionFour() {
    EntityMention res = mock(EntityMention.class);
    when(res.getSentiment().getMagnitude()).thenReturn(0.8f);
    when(res.getSentiment().getScore()).thenReturn(0f);
    return res;
  }
}
