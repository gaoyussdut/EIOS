package top.toptimus;

import com.google.common.collect.Lists;
import top.toptimus.common.enums.FkeyTypeEnum;
import top.toptimus.place.PlaceDTO;
import top.toptimus.tokendata.TokenDataDto;
import top.toptimus.tokendata.field.FkeyField;

import java.util.*;

public class Testing {

    public static void main(String[] args) {

//        HashMap<String, Long> map = new HashMap<>();
//
//        map.put("A", (long) 99);
//        map.put("B", (long) 67);
//        map.put("C", (long) 109);
//        map.put("D", (long) 2);
//
//        System.out.println("unsorted map: " + map);
//
//        List<Map.Entry<String, Long>> list = new ArrayList<>(map.entrySet());
//        list.sort((o1, o2) -> (int) (o2.getValue() - o1.getValue()));
//
//        System.out.println("results: " + list);


        String billTokenId = UUID.randomUUID().toString();
        String metaId = "meta1";
        String fkey = "卧槽";
        PlaceDTO placeDTO = initData(billTokenId, metaId, fkey);


        System.out.println("results: " +
                getTokensOrder(
                        placeDTO.getDatas().get(metaId)
                        , fkey
                        , 0
                        , 10
                )
        );
    }

    private static List<String> getTokensOrder(
            List<TokenDataDto> tokenDataDtos
            , String fKey
            , int fromIndex
            , int toIndex
    ) {
        List<Map.Entry<String, String>> tokenList = new ArrayList<>(
                convertToMap(tokenDataDtos, fKey).entrySet()
        );
        tokenList.sort((o1, o2) -> (o1.getValue().compareTo(o2.getValue())));

        return new ArrayList<String>() {{
            tokenList.subList(fromIndex, toIndex).forEach(tokenEntry -> {
                add(tokenEntry.getKey());
            });
        }};
    }

    private static Map<String, String> convertToMap(List<TokenDataDto> tokenDataDtos, String fKey) {
        return new HashMap<String, String>() {{
            for (TokenDataDto tokenDataDto : tokenDataDtos) {
                for (FkeyField fkeyField : tokenDataDto.getFields()) {
                    if (fKey.equals(fkeyField.getKey())) {
                        put(tokenDataDto.getTokenId(), fkeyField.getJsonData());
                        break;
                    }
                }
            }
        }};
    }

    private static PlaceDTO initData(String billTokenId, String metaId, String fkey) {
        Map<String, List<TokenDataDto>> datas = new HashMap<String, List<TokenDataDto>>() {
            {
                put(metaId
                        , new ArrayList<TokenDataDto>() {
                            {
                                for (int i = 0; i < 50; i++) {
                                    add(
                                            new TokenDataDto(
                                                    String.valueOf(i)
                                                    , Lists.newArrayList(new FkeyField().createPlainFkeyField(FkeyTypeEnum.INTEGER, fkey, fkey + String.valueOf(i)))
                                            )
                                    );
                                }
                            }
                        });
                put("meta2"
                        , new ArrayList<TokenDataDto>() {
                            {
                                for (int i = 50; i < 100; i++) {
                                    add(
                                            new TokenDataDto(
                                                    String.valueOf(i)
                                                    , Lists.newArrayList(new FkeyField().createPlainFkeyField(FkeyTypeEnum.INTEGER, fkey, fkey + String.valueOf(i)))
                                            )
                                    );
                                }
                            }
                        });
            }
        };

        return new PlaceDTO(billTokenId, datas);

    }
}
