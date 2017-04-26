package com.hs.gms.srv.api.autocomplete;

/**
 * AutoCompleteCoreType
 * 
 * @author BH Jun
 */
public enum AutoCompleteType {
    CODE {

        @Override
        public AutoCompleteTypeVO selectAutoCompleteData() {
            AutoCompleteTypeVO result = new AutoCompleteTypeVO();
            String[] codeSubParamArr = {"pCode"};

            result.setCore(CODE_CORE);
            result.setActionPrefix(SELECT_PREFIX);
            result.setSubParamNameArr(codeSubParamArr);

            return result;
        }

        @Override
        public AutoCompleteTypeVO importAutoCompleteData() {
            AutoCompleteTypeVO result = new AutoCompleteTypeVO();

            result.setCore(CODE_CORE);
            result.setActionPrefix(IMPORT_PREFIX);

            return result;
        }

        @Override
        public AutoCompleteTypeVO updateAutoCompleteData() {
            AutoCompleteTypeVO result = new AutoCompleteTypeVO();

            result.setCore(CODE_CORE);
            result.setActionPrefix(UPDATE_PREFIX);

            return result;
        }
    },
    USER {

        @Override
        public AutoCompleteTypeVO selectAutoCompleteData() {
            AutoCompleteTypeVO result = new AutoCompleteTypeVO();
            String[] codeSubParamArr = {"tenantId"};

            result.setCore(USER_CORE);
            result.setActionPrefix(SELECT_PREFIX);
            result.setSubParamNameArr(codeSubParamArr);

            return result;
        }

        @Override
        public AutoCompleteTypeVO importAutoCompleteData() {
            AutoCompleteTypeVO result = new AutoCompleteTypeVO();

            result.setCore(USER_CORE);
            result.setActionPrefix(IMPORT_PREFIX);

            return result;
        }

        @Override
        public AutoCompleteTypeVO updateAutoCompleteData() {
            AutoCompleteTypeVO result = new AutoCompleteTypeVO();

            result.setCore(USER_CORE);
            result.setActionPrefix(UPDATE_PREFIX);

            return result;
        }

    };

    public abstract AutoCompleteTypeVO selectAutoCompleteData();

    public abstract AutoCompleteTypeVO importAutoCompleteData();

    public abstract AutoCompleteTypeVO updateAutoCompleteData();

    private static final String SELECT_PREFIX = "/select";
    private static final String IMPORT_PREFIX = "/import";
    private static final String UPDATE_PREFIX = "/update";

    private static final String CODE_CORE = "/gmscode";
    private static final String USER_CORE = "/gmsuser";
}
