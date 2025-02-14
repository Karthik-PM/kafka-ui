package com.provectus.kafka.ui.serdes.builtin;

import com.google.common.primitives.Ints;
import com.google.common.primitives.UnsignedInteger;
import com.provectus.kafka.ui.serde.api.DeserializeResult;
import com.provectus.kafka.ui.serde.api.PropertyResolver;
import com.provectus.kafka.ui.serde.api.SchemaDescription;
import com.provectus.kafka.ui.serdes.BuiltInSerde;
import java.util.Map;
import java.util.Optional;

public class UInt32Serde implements BuiltInSerde {

  public static String name() {
    return "UInt32";
  }

  @Override
  public void configure(PropertyResolver serdeProperties,
                        PropertyResolver kafkaClusterProperties,
                        PropertyResolver globalProperties) {
  }

  @Override
  public Optional<String> getDescription() {
    return Optional.empty();
  }

  @Override
  public Optional<SchemaDescription> getSchema(String topic, Target type) {
    return Optional.of(
        new SchemaDescription(
            String.format(
                "{ "
                    + "  \"type\" : \"integer\", "
                    + "  \"minimum\" : 0, "
                    + "  \"maximum\" : %s"
                    + "}",
                UnsignedInteger.MAX_VALUE
            ),
            Map.of()
        )
    );
  }

  @Override
  public boolean canDeserialize(String topic, Target type) {
    return true;
  }

  @Override
  public boolean canSerialize(String topic, Target type) {
    return true;
  }

  @Override
  public Serializer serializer(String topic, Target type) {
    return input -> Ints.toByteArray(Integer.parseUnsignedInt(input));
  }

  @Override
  public Deserializer deserializer(String topic, Target type) {
    return (headers, data) ->
        new DeserializeResult(
            UnsignedInteger.fromIntBits(Ints.fromByteArray(data)).toString(),
            DeserializeResult.Type.JSON,
            Map.of()
        );
  }
}
